package md.step.BlocAdmin.api;

import md.step.BlocAdmin.exception.MetersNotFoundException;
import md.step.BlocAdmin.model.*;
import md.step.BlocAdmin.repository.*;
import md.step.BlocAdmin.service.FlatsService;
import md.step.BlocAdmin.service.MetersService;
import md.step.BlocAdmin.service.SuppliersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/meters")
public class MetersController {
public final MetersService metersService;
public final SuppliersService suppliersService;
public final FlatsService flatsService;

    @Autowired
    private MetersRepository metersRepository;
    @Autowired
    private MeterDestRepository meterDestRepository;
    @Autowired
    private TypeOfMeterInvoiceRepository typeOfMeterInvoiceRepository;
    @Autowired
    private SuppliersRepository suppliersRepository;
    @Autowired
    private FlatsRepository flatsRepository;
    @Autowired
    private BuildingsRepository buildingsRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired

    public MetersController(MetersService metersService, SuppliersService suppliersService, FlatsService flatsService, MeterDestRepository meterDestRepository,
                            TypeOfMeterInvoiceRepository typeOfMeterInvoiceRepository,BuildingsRepository buildingsRepository) {
        this.metersService = metersService;
        this.suppliersService = suppliersService;
        this.flatsService = flatsService;
        this.meterDestRepository = meterDestRepository;
        this.typeOfMeterInvoiceRepository = typeOfMeterInvoiceRepository;
        this.buildingsRepository=buildingsRepository;
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllMeters(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Meters> meters = new ArrayList<Meters>();
            Pageable paging = PageRequest.of(page, size);

            Page<Meters> pageMeters;
            if (title == null) {
                pageMeters = metersRepository.findAll(paging);
            } else {
                pageMeters = metersRepository.findAll(paging);
//                pagePersons = personRepository.findByNameStartingWithOrSurnameStartingWithOrIdnpStartingWithOrEmailStartingWithOrPhoneStartingWithOrMobileStartingWith(title, title, title, title,title,title, paging);

            }

            meters = pageMeters.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("meters", meters);
            response.put("currentPage", pageMeters.getNumber());
            response.put("totalItems", pageMeters.getTotalElements());
            response.put("totalPages", pageMeters.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("fm")
    public ResponseEntity<List<Meters>> getAllFilteredMeters(
            @RequestParam(required = false) String supp,
            @RequestParam(required = false) Integer ladd,
            @RequestParam(required = false) Integer build
    ) {
        System.out.println("Supp="+supp);
        System.out.println("ladd="+ladd);
        System.out.println("build="+build);
        try {
            List<Meters> meters = new ArrayList<Meters>();

            if (ladd == null) {
                meters = metersRepository.findAllBySupplier_SupplierNameAndFlat_Building_Buildingid(supp,build);
            } else {
                meters = metersRepository.findAllBySupplier_SupplierNameAndFlat_Building_BuildingidAndAndFlat_Entrance(supp,ladd,build);
            }

            return new ResponseEntity<>(meters, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("suppliers")
    public ResponseEntity<List<Suppliers>> getSuppliers() {
        List<Suppliers> suppliers = metersService.findAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("persons")
    public ResponseEntity<List<Person>> getPersons() {
        List<Person> persons = metersService.findAllPerson();
        return ResponseEntity.ok(persons);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("buildingflats/{id}")
    public ResponseEntity<List<Flats>> getBuildingFlats(@PathVariable("id") Integer id) {
        Buildings buildings = buildingsRepository.findAllByBuildingid(id);
        List<Flats> flats = metersService.getFlatByBuilding(buildings);
        return ResponseEntity.ok(flats);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("flats")
    public ResponseEntity<List<Flats>> getFlats() {
        List<Flats> flats = metersService.findAllFlats();
        return ResponseEntity.ok(flats);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("buildings")
    public ResponseEntity<List<Buildings>> getBuildings() {
        List<Buildings> buildings = metersService.findAllBuildings();
        return ResponseEntity.ok(buildings);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("metertype")
    public ResponseEntity<List<MeterDest>> getMeterDest() {
        List<MeterDest> meterDest = metersService.findAllMeterType();
        return ResponseEntity.ok(meterDest);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("typeofmeterinvoice")
    public ResponseEntity<List<TypeOfMeterInvoice>> getTypeMeterAndInvoice() {
        List<TypeOfMeterInvoice> typeOfMeterAndInvoices = metersService.findAllTypeOfMeterInvoice();
        return ResponseEntity.ok(typeOfMeterAndInvoices);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Meters> getMeterById(@PathVariable("id") Integer id) throws MetersNotFoundException {
        Meters meter = metersService.findMeterById(id);
        return new ResponseEntity<>(meter, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @PostMapping()
    public ResponseEntity<Meters> addMeter(@RequestBody Meters meter) {
        Suppliers supplier =suppliersRepository.findAllBySupplierid(meter.getSupplier().getSupplierId());
        meter.setSupplier(supplier);


        if(meter.getPerson()!=null) {
            Person person = personRepository.findAllByPersonid(meter.getPerson().getPersonid());
            meter.setPerson(person);
        }

        if(meter.getFlat()!=null) {
            Flats flat = flatsRepository.findAllByFlatid(meter.getFlat().getFlatid());
            meter.setFlat(flat);
        }

        if(meter.getBuilding()!=null) {
            Buildings building = buildingsRepository.findAllByBuildingid(meter.getBuilding().getBuildingid());
            meter.setBuilding(building);
        }


        MeterDest meterDestination= meterDestRepository.findAllByMetertypeid(meter.getMeterDest().getId());
        meter.setMeterDest(meterDestination);

        TypeOfMeterInvoice typeOfMeterInvoice = typeOfMeterInvoiceRepository.findAllById(meter.getTypeOfMeterInvoice().getId());
        meter.setTypeOfMeterInvoice(typeOfMeterInvoice);

        System.out.println("Meters = "+meter.getSerial());
        metersService.addMeter(meter);

        return new ResponseEntity<>(meter, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @PutMapping()
    public ResponseEntity<Meters> updateMeter(@RequestBody Meters meter) {
        Suppliers supplier;
        supplier=suppliersRepository.findAllBySupplierid(meter.getSupplier().getSupplierId());
        meter.setSupplier(supplier);

        if(meter.getPerson()!=null) {
            Person person= personRepository.findAllByPersonid(meter.getPerson().getPersonid());
            meter.setPerson(person);
        }

        if(meter.getFlat()!=null) {
            Flats flat= flatsRepository.findAllByFlatid(meter.getFlat().getFlatid());
            meter.setFlat(flat);
        }
        if(meter.getBuilding()!=null) {
            Buildings building = buildingsRepository.findAllByBuildingid(meter.getBuilding().getBuildingid());
            meter.setBuilding(building);
        }


        MeterDest meterDestination = new MeterDest();
        meterDestination= meterDestRepository.findAllByMetertypeid(meter.getMeterDest().getId());
        meter.setMeterDest(meterDestination);

        Meters updateMeter = metersService.updateMeter(meter);
        return new ResponseEntity<>(updateMeter, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeter(@PathVariable("id") Integer id) throws MetersNotFoundException {
        metersService.deleteMeter(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
