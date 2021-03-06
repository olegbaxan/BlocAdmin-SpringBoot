package md.step.BlocAdmin.api;

import md.step.BlocAdmin.exception.PersonNotFoundException;
import md.step.BlocAdmin.model.*;
import md.step.BlocAdmin.repository.*;
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

@CrossOrigin(maxAge = 3600, allowCredentials = "true", origins = "https://blocadmin-angularui.herokuapp.com/")
@RestController
@RequestMapping("/api/v1/person")
public class PersonController {
    private final PersonService personService;

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MetersRepository metersRepository;
    @Autowired
    private FlatsRepository flatsRepository;
    @Autowired
    private PaymentsRepository paymentsRepository;


    @Autowired
    public PersonController(PersonService personService, RoleRepository roleRepository, PaymentsRepository paymentsRepository,
                            FlatsRepository flatsRepository, MetersRepository metersRepository) {
        this.personService = personService;
        this.roleRepository = roleRepository;
        this.paymentsRepository = paymentsRepository;
        this.metersRepository = metersRepository;
        this.flatsRepository = flatsRepository;

    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
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
                pagePersons = personRepository.findAllByRoles(personRole, paging);
//                pagePersons = personRepository.findAll(paging);
            } else {
                pagePersons = personRepository.findAllByRolesAndNameContainingIgnoreCaseOrSurnameContainingIgnoreCaseOrIdnpStartingWithIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneStartingWithOrMobileStartingWith(personRole, title, title, title, title, title, title, paging);
//                pagePersons = personRepository.findByNameStartingWithOrSurnameStartingWithOrIdnpStartingWithOrEmailStartingWithOrPhoneStartingWithOrMobileStartingWith(title, title, title, title,title,title, paging);
            }

            persons = pagePersons.getContent();
            persons = persons.stream().filter(p -> p.getRoles().contains(personRole.get())).collect(Collectors.toList());
            Map<String, Object> response = new HashMap<>();
            response.put("persons", persons);
            response.put("currentPage", pagePersons.getNumber());
            response.put("totalItems", pagePersons.getTotalElements());
            response.put("totalPages", pagePersons.getTotalPages());

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
        if (strRoles.isEmpty()) {
            Role personRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
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

    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable("id") Integer id) throws PersonNotFoundException {
        try {
            //Check person flat, meter,payments, role==ROLE_USER
            Person person = personRepository.findAllByPersonid(id);
            List<Flats> flats = flatsRepository.findFlatsByPerson(person);
            List<Meters> meters = metersRepository.findAllByPerson(person);
            List<Payments> payments = paymentsRepository.findAllByPerson(person);

            if (flats.size() > 0 || meters.size() > 0 || payments.size() > 0) {
                throw new Exception("Person cannot be deleted");
            }

            personService.deletePerson(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

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
