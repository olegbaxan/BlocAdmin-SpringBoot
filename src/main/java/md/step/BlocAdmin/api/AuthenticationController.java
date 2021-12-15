package md.step.BlocAdmin.api;

import md.step.BlocAdmin.exception.TokenRefreshException;
import md.step.BlocAdmin.model.ERole;
import md.step.BlocAdmin.model.Person;
import md.step.BlocAdmin.model.RefreshToken;
import md.step.BlocAdmin.model.Role;
import md.step.BlocAdmin.payload.request.LoginRequest;
import md.step.BlocAdmin.payload.request.SignupRequest;
import md.step.BlocAdmin.payload.request.TokenRefreshRequest;
import md.step.BlocAdmin.payload.response.JwtResponse;
import md.step.BlocAdmin.payload.response.MessageResponse;
import md.step.BlocAdmin.payload.response.TokenRefreshResponse;
import md.step.BlocAdmin.repository.PersonRepository;
import md.step.BlocAdmin.repository.RoleRepository;
import md.step.BlocAdmin.security.jwt.JwtUtils;
import md.step.BlocAdmin.security.services.PersonDetailsImpl;
import md.step.BlocAdmin.security.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(maxAge = 3600, allowCredentials = "true",origins = "https://blocadmin-angularui.herokuapp.com/")
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        PersonDetailsImpl userDetails = (PersonDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getPersonId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getPersonId(),
                userDetails.getUsername(), userDetails.getEmail(), roles));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (personRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

//        if (personRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already in use!"));
//        }

        if (personRepository.existsByIdnp(signUpRequest.getIdnp())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: IDNP is already in use!"));
        }

        // Create new user's account
        Person person = new Person(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getDescription(),
                signUpRequest.getName(),
                signUpRequest.getSurname(),
                signUpRequest.getIdnp(),
                signUpRequest.getMobile());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role personRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            System.out.println("personRole = "+personRole);
            roles.add(personRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_BLOCADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
//        System.out.println("Roles = "+roles);
        person.setRoles(roles);
        personRepository.save(person);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getPerson)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}
