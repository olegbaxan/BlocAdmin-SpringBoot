package md.step.BlocAdmin.api;

import md.step.BlocAdmin.exception.FlatsNotFoundException;
import md.step.BlocAdmin.model.*;
import md.step.BlocAdmin.repository.*;
import md.step.BlocAdmin.service.BuidingsService;
import md.step.BlocAdmin.service.FlatsService;
import md.step.BlocAdmin.service.MetersService;
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

@CrossOrigin(maxAge = 3600, allowCredentials = "true",origins = "*")
@RestController
@RequestMapping("/api/v1/flats")
public class FlatsController {
    private final FlatsService flatsService;
    private final BuidingsService buidingsService;
    private final PersonService personService;
    private final MetersService metersService;

    @Autowired
    private FlatsRepository flatsRepository;
    @Autowired
    private BuildingsRepository buildingsRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MetersRepository metersRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    public FlatsController(FlatsService flatsService, BuidingsService buidingsService, PersonService personService,
                           MetersService metersService, RoleRepository roleRepository) {
        this.flatsService = flatsService;
        this.buidingsService = buidingsService;
        this.personService = personService;
        this.metersService = metersService;
        this.roleRepository=roleRepository;
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllFLats(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Flats> flats = new ArrayList<Flats>();
            Pageable paging = PageRequest.of(page, size);

            Page<Flats> pageFlats;
            if (title == null) {
                pageFlats = flatsRepository.findAll(paging);
            } else {
//                pageFlats = flatsRepository.findAll(paging);
                pageFlats = flatsRepository.findAllDistinctFlatsByBuilding_Address_CityStartingWithIgnoreCaseOrBuilding_Address_RaionStartingWithIgnoreCaseOrBuilding_Address_StreetStartingWithIgnoreCaseOrPerson_NameStartingWithIgnoreCaseOrPerson_SurnameStartingWithIgnoreCase( title,title,title,title,title, paging);

            }

            flats = pageFlats.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("flats", flats);
            response.put("currentPage", pageFlats.getNumber());
            response.put("totalItems", pageFlats.getTotalElements());
            response.put("totalPages", pageFlats.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("available/{id}")
    public ResponseEntity<List<Integer>> getAllAvailableFLatsNumbers(@PathVariable("id") Integer id) {
        try {
            List<Flats> flats = new ArrayList<Flats>();
            List<Integer> availableFlats = new ArrayList<>();
            Buildings building=new Buildings();
            if (id>0) {
                building = buildingsRepository.findAllByBuildingid(id);
                flats=flatsRepository.findFlatsByBuilding(building);
            }
            if (building.getFlats()>0){
                System.out.println("flatBuilding = "+building.getFlats()+1);
                for (int i=1;i<building.getFlats()+1;i++){
                    boolean used=false;
                    for (int j=0;j<flats.size();j++){
                        if (i==flats.get(j).getFlatNumber()){
                            used=true;
                            break;
                        }
                    }
                    if (!used){
                        System.out.println("available = "+i);
                        availableFlats.add(i);
                    }
                }
            }

            return new ResponseEntity<>(availableFlats,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("buildings")
    public ResponseEntity<List<Buildings>> getBuildings() {
        List<Buildings> buildings = flatsService.findAllBuildings();
        return ResponseEntity.ok(buildings);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("persons")
    public ResponseEntity<List<Person>> getPersons() {
        List<Person> persons = flatsService.findAllPerson();
        persons = persons.stream().filter(p -> p.getRoles().contains(roleRepository.findByName(ERole.ROLE_USER).get())).collect(Collectors.toList());


        return ResponseEntity.ok(persons);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("meters")
    public ResponseEntity<List<Meters>> getMeters() {
        List<Meters> meters = flatsService.findAllMeters();
        return ResponseEntity.ok(meters);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("allnegativewallet")
    public ResponseEntity<List<Flats>> getFlatsNegativeWallet() {
        List<Flats> flats = flatsRepository.findFlatsByWalletLessThan(0.0);//negative Wallet
        return ResponseEntity.ok(flats);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("/{id}")
    public ResponseEntity<Flats> getFlatById(@PathVariable("id") Integer id) throws FlatsNotFoundException {
        Flats flat = flatsService.findFlatById(id);
        return new ResponseEntity<>(flat, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @PostMapping()
    public ResponseEntity<Flats> addFlat(@RequestBody Flats flat) {
        Buildings building = new Buildings();
        building=buildingsRepository.findAllByBuildingid(flat.getBuilding().getBuildingid());
        flat.setBuilding(building);

        Set<Meters> meters = new HashSet<>();
        Set<Meters> flatMeters = flat.getMeters();
        if(flatMeters!=null){
            flatMeters.forEach(meter -> {
                Meters meterToAdd = metersRepository.findAllByMeterid(meter.getMeterId());
                meters.add(meterToAdd);
            });
            flat.setMeters(meters);
        }


        Set<Person> persons = new HashSet<>();
        Set<Person> personId = flat.getPerson();
        personId.forEach(person -> {
            Person personToAdd = personRepository.findAllByPersonid(person.getPersonid());
            persons.add(personToAdd);
        });
        flat.setPerson(persons);

        flatsService.addFlat(flat);

        return new ResponseEntity<>(flat, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @PutMapping()
    public ResponseEntity<Flats> updateFlat(@RequestBody Flats flat) {
        Buildings building = new Buildings();
        building=buildingsRepository.findAllByBuildingid(flat.getBuilding().getBuildingid());
        flat.setBuilding(building);

        Set<Meters> meters = new HashSet<>();
        Set<Meters> metersId = flat.getMeters();
        if(metersId!=null){
        metersId.forEach(meter -> {
            Meters meterToAdd = metersRepository.findAllByMeterid(meter.getMeterId());
            meters.add(meterToAdd);
        });
        flat.setMeters(meters);
        }

        Set<Person> persons = new HashSet<>();
        Set<Person> flatPerson = flat.getPerson();
        flatPerson.forEach(person -> {
            Person personToAdd = personRepository.findAllByPersonid(person.getPersonid());
            persons.add(personToAdd);
        });
        flat.setPerson(persons);
        Flats updateFlat = flatsService.updateFlat(flat);
        return new ResponseEntity<>(updateFlat, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFlat(@PathVariable("id") Integer id) throws FlatsNotFoundException {
        flatsService.deleteFlat(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
