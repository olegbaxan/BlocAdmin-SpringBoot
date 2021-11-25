package md.step.BlocAdmin.api;

import md.step.BlocAdmin.exception.PersonNotFoundException;
import md.step.BlocAdmin.model.ERole;
import md.step.BlocAdmin.model.Person;
import md.step.BlocAdmin.model.Role;
import md.step.BlocAdmin.repository.PersonRepository;
import md.step.BlocAdmin.repository.RoleRepository;
import md.step.BlocAdmin.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/person")
public class PersonController {
    private final PersonService personService;

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    public PersonController(PersonService personService,RoleRepository roleRepository) {
        this.personService = personService;
        this.roleRepository=roleRepository;
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllPersons(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Person> persons = new ArrayList<Person>();
            Pageable paging = PageRequest.of(page, size);
            Optional<Role> personRole = roleRepository.findByName(ERole.ROLE_USER);
            Page<Person> pagePersons;
            if (title == null) {
//                Optional<Role> personRole = roleRepository.findByName(ERole.ROLE_USER);
                pagePersons = personRepository.findAllByRoles(personRole,paging);
//                pagePersons = personRepository.findAll(paging);
            } else {
                pagePersons = personRepository.findAllByRolesAndNameStartingWithOrSurnameStartingWithOrIdnpStartingWithOrEmailStartingWithOrPhoneStartingWithOrMobileStartingWith(personRole,title, title, title, title,title,title, paging);
//                pagePersons = personRepository.findByNameStartingWithOrSurnameStartingWithOrIdnpStartingWithOrEmailStartingWithOrPhoneStartingWithOrMobileStartingWith(title, title, title, title,title,title, paging);
            }

            persons = pagePersons.getContent();
            persons = persons.stream().filter(p -> p.getRoles().contains(personRole.get())).collect(Collectors.toList());
            Map<String, Object> response = new HashMap<>();
            response.put("persons", persons);
            response.put("currentPage", pagePersons.getNumber());
            response.put("totalItems", pagePersons.getTotalElements());
            response.put("totalPages", pagePersons.getTotalPages());

            System.out.println("response = " + response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("roles")
    public ResponseEntity<List<Role>> getRoles() {
            List<Role> role = personService.findAllRoles();
        return ResponseEntity.ok(role);
    }
    @GetMapping("username/{username}")
    public Boolean checkUsername(@PathVariable("username") String username) {
        return this.personService.checkUsernameExist(username);
    }
    @GetMapping("idnp/{idnp}")
    public Boolean checkIdnp(@PathVariable("idnp") String idnp) {
        return this.personService.checkIdnpExist(idnp);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable("id") Integer id) throws PersonNotFoundException {
        Person person = personService.findPersonById(id);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        Set<Role> strRoles = person.getRoles();
        Set<Role> roles = new HashSet<>();
        System.out.println("Roles = "+strRoles);
        if (strRoles.isEmpty()) {
            Role personRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            System.out.println("personRole = "+personRole);
            roles.add(personRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.getName()) {
                    case ROLE_ADMIN:
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case ROLE_BLOCADMIN:
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
        person.setRoles(roles);
        personRepository.save(person);

        return new ResponseEntity<>(person, HttpStatus.OK);
    }
    @PutMapping()
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        Set<Role> strRoles = person.getRoles();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role personRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//            System.out.println("personRole = "+personRole.getName());
            roles.add(personRole);
        } else {

            strRoles.forEach(role -> {
                switch (role.getName()) {
                    case ROLE_ADMIN:
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case ROLE_BLOCADMIN:
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
        person.setRoles(roles);
        Person updatePerson = personService.updatePerson(person);
        return new ResponseEntity<>(updatePerson, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable("id") Integer id) throws PersonNotFoundException {
        personService.deletePerson(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/person")
    @PreAuthorize("hasRole('USER') or hasRole('BLOCADMIN') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('BLOCADMIN')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
