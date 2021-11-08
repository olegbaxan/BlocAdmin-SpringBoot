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
    private MeterTypeRepository meterTypeRepository;
    @Autowired
    private TypeOfMeterAndInvoiceRepository typeOfMeterAndInvoiceRepository;
    @Autowired
    private SuppliersRepository suppliersRepository;
    @Autowired
    private FlatsRepository flatsRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired

    public MetersController(MetersService metersService, SuppliersService suppliersService, FlatsService flatsService, MeterTypeRepository meterTypeRepository,
                            TypeOfMeterAndInvoiceRepository typeOfMeterAndInvoiceRepository) {
        this.metersService = metersService;
        this.suppliersService = suppliersService;
        this.flatsService = flatsService;
        this.meterTypeRepository = meterTypeRepository;
        this.typeOfMeterAndInvoiceRepository = typeOfMeterAndInvoiceRepository;
    }
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
    @GetMapping("supplier")
    public ResponseEntity<List<Suppliers>> getSuppliers() {
        List<Suppliers> suppliers = metersService.findAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }
    @GetMapping("person")
    public ResponseEntity<List<Person>> getPersons() {
        List<Person> persons = metersService.findAllPerson();
        return ResponseEntity.ok(persons);
    }
    @GetMapping("flats")
    public ResponseEntity<List<Flats>> getFlats() {
        List<Flats> flats = metersService.findAllFlats();
        return ResponseEntity.ok(flats);
    }
    @GetMapping("metertype")
    public ResponseEntity<List<MeterType>> getMeterDest() {
        List<MeterType> meterType = metersService.findAllMeterType();
        return ResponseEntity.ok(meterType);
    }
    @GetMapping("typeofmeterandinvoice")
    public ResponseEntity<List<TypeOfMeterAndInvoice>> getTypeMeterAndInvoice() {
        List<TypeOfMeterAndInvoice> typeOfMeterAndInvoices = metersService.findAllTyepOfMeterAndInvoice();
        return ResponseEntity.ok(typeOfMeterAndInvoices);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Meters> getMeterById(@PathVariable("id") Integer id) throws MetersNotFoundException {
        Meters meter = metersService.findMeterById(id);
        return new ResponseEntity<>(meter, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Meters> addMeter(@RequestBody Meters meter) {
        Suppliers supplier = new Suppliers();
        supplier=suppliersRepository.findAllBySupplierid(meter.getSupplier().getSupplierId());
        meter.setSupplier(supplier);

        Person person = new Person();
        person=personRepository.findAllByPersonid(meter.getPerson().getPersonid());
        meter.setPerson(person);

        Flats flat = new Flats();
        flat=flatsRepository.findAllByFlatid(meter.getFlat().getFlatid());
        meter.setFlat(flat);

        MeterType meterDestination = new MeterType();
        meterDestination= meterTypeRepository.findAllByMetertypeid(meter.getMeterType().getId());
        meter.setMeterType(meterDestination);

        TypeOfMeterAndInvoice typeOfMeterAndInvoice = new TypeOfMeterAndInvoice();
        typeOfMeterAndInvoice= typeOfMeterAndInvoiceRepository.findAllById(meter.getTypeOfMeterAndInvoice().getId());
        meter.setTypeOfMeterAndInvoice(typeOfMeterAndInvoice);

        metersService.addMeter(meter);

        return new ResponseEntity<>(meter, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Meters> updateMeter(@RequestBody Meters meter) {
        Suppliers supplier = new Suppliers();
        supplier=suppliersRepository.findAllBySupplierid(meter.getSupplier().getSupplierId());
        meter.setSupplier(supplier);

        Flats flat = new Flats();
        flat=flatsRepository.findAllByFlatid(meter.getFlat().getFlatid());
        meter.setFlat(flat);
        MeterType meterDestination = new MeterType();
        meterDestination= meterTypeRepository.findAllByMetertypeid(meter.getMeterType().getId());
        meter.setFlat(flat);

        Meters updateMeter = metersService.updateMeter(meter);
        return new ResponseEntity<>(updateMeter, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeter(@PathVariable("id") Integer id) throws MetersNotFoundException {
        metersService.deleteMeter(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
