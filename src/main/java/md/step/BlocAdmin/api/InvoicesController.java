package md.step.BlocAdmin.api;

import md.step.BlocAdmin.exception.InvoicesNotFoundException;
import md.step.BlocAdmin.message.ResponseFile;
import md.step.BlocAdmin.model.*;
import md.step.BlocAdmin.repository.*;
import md.step.BlocAdmin.service.FileStorageService;
import md.step.BlocAdmin.service.InvoicesService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/invoices")
public class InvoicesController {
    private final InvoicesService invoicesService;
    private final SuppliersService suppliersService;
    private final MetersService metersService;
    private final FileStorageService fileStorageService;

    @Autowired
    private InvoicesRepository invoicesRepository;
    @Autowired
    private SuppliersRepository suppliersRepository;
    @Autowired
    private MetersRepository metersRepository;
    @Autowired
    private TypeOfMeterInvoiceRepository typeOfMeterInvoiceRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private BuildingsRepository buildingsRepository;
    @Autowired
    private FlatsRepository flatsRepository;
    @Autowired
    private FileDBRepository fileDBRepository;
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    public InvoicesController(InvoicesService invoicesService, SuppliersService suppliersService, MetersService metersService,
                              TypeOfMeterInvoiceRepository typeOfMeterInvoiceRepository, FileStorageService fileStorageService,
                              StatusRepository statusRepository, BuildingsRepository buildingsRepository, FlatsRepository flatsRepository,
                              PersonRepository personRepository) {
        this.invoicesService = invoicesService;
        this.suppliersService = suppliersService;
        this.metersService = metersService;
        this.personRepository = personRepository;
        this.buildingsRepository = buildingsRepository;
        this.typeOfMeterInvoiceRepository = typeOfMeterInvoiceRepository;
        this.fileStorageService = fileStorageService;
        this.statusRepository = statusRepository;
        this.flatsRepository = flatsRepository;
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllInvoices(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Invoices> invoices = new ArrayList<Invoices>();
            Pageable paging = PageRequest.of(page, size);

            Page<Invoices> pageInvoices;
            if (title == null) {
                pageInvoices = invoicesRepository.findAll(paging);
            } else {
                pageInvoices = invoicesRepository.findAll(paging);
//                pagePersons = personRepository.findByNameStartingWithOrSurnameStartingWithOrIdnpStartingWithOrEmailStartingWithOrPhoneStartingWithOrMobileStartingWith(title, title, title, title,title,title, paging);

            }

            invoices = pageInvoices.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("invoices", invoices);
            response.put("currentPage", pageInvoices.getNumber());
            response.put("totalItems", pageInvoices.getTotalElements());
            response.put("totalPages", pageInvoices.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("suppliers")
    public ResponseEntity<List<Suppliers>> getSuppliers() {
        List<Suppliers> suppliers = invoicesService.findAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("buildings")
    public ResponseEntity<List<Buildings>> getBuildings() {
        List<Buildings> buildings = invoicesService.findAllBuildings();
        return ResponseEntity.ok(buildings);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("meters")
    public ResponseEntity<List<Meters>> getMeters() {
        List<Meters> meters = invoicesService.findAllMeters();
        return ResponseEntity.ok(meters);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("file/{id}")
    public ResponseEntity<Map<String, Object>> getFile(@PathVariable("id") String name) {
        Map<String, Object> response = new HashMap<>();
        if (name.length()>0){
            FileDB invoiceFile = invoicesService.getFileByName(name);
//        }
//        if (invoiceFile.getName().length()>0) {
            List<ResponseFile> files = fileDBRepository.findAll().stream()
                    .filter(c -> c.getName().equals(invoiceFile.getName()))
                    .map(dbFile -> {
                        String fileDownloadUri = ServletUriComponentsBuilder
                                .fromCurrentContextPath()
                                .path("/api/v1/files/")
                                .path(dbFile.getFileid())
                                .toUriString();

                        return new ResponseFile(
                                dbFile.getName(),
                                fileDownloadUri,
                                dbFile.getType(),
                                dbFile.getData().length);
                    }).collect(Collectors.toList());
            response.put("invoiceFile", invoiceFile);
            response.put("fileInfo", files);
        }


//            return ResponseEntity.status(HttpStatus.OK).body(files);


        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("typeofmeterandinvoice")
    public ResponseEntity<List<TypeOfMeterInvoice>> getTypeOfMeterAndInvoice() {
        List<TypeOfMeterInvoice> type = invoicesService.findAllTyepOfMeterAndInvoice();
        return ResponseEntity.ok(type);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("/{id}")
    public ResponseEntity<Invoices> getInvoiceById(@PathVariable("id") Integer id) throws InvoicesNotFoundException {
        Invoices invoice = invoicesService.findInvoiceById(id);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @GetMapping("personinvoices/{id}")
    public ResponseEntity<List<Invoices>> getInvoiceByPersonId(@PathVariable("id") Integer id) throws InvoicesNotFoundException {
        Person person = personRepository.findAllByPersonid(id);
        System.out.println("Person Invoice ="+person);
        List<Invoices> invoice = invoicesRepository.findFirst10InvoicesByMeter_PersonOrMeter_Flat_PersonOrderByEmittedDateDesc(person,person);

        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }
    @GetMapping("supplierinvoices")
    public ResponseEntity<List<Invoices>> getInvoiceBySupplier() {
        TypeOfMeterInvoice typeOfMeterInvoice = typeOfMeterInvoiceRepository.findAllById(1);//TYPE_BUILDING
        Person person=personRepository.findAllByPersonid(8);
        System.out.println("Type= "+ typeOfMeterInvoice.getName());
        List<Invoices> invoice = invoicesRepository.findFirst10InvoicesByTypeOfMeterInvoiceOrderByEmittedDateDesc(typeOfMeterInvoice);
//        List<Invoices> invoice = invoicesRepository.findFirst10InvoicesByMeter_Flat_PersonAndTypeOfMeterInvoiceOrderByEmittedDateDesc(person,typeOfMeterInvoice);

        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @PostMapping()
    public ResponseEntity<Invoices> addInvoice(@RequestBody Invoices invoice) {
        Suppliers supplier = new Suppliers();
        supplier = suppliersRepository.findAllBySupplierid(invoice.getSupplier().getSupplierId());
        invoice.setSupplier(supplier);

        TypeOfMeterInvoice typeOfMeterInvoice = typeOfMeterInvoiceRepository.findAllById(invoice.getTypeOfMeterInvoice().getId());
        invoice.setTypeOfMeterInvoice(typeOfMeterInvoice);


        if (invoice.getBuildings() != null) {
            Set<Buildings> buildings = new HashSet<>();
            Set<Buildings> strBuilding = invoice.getBuildings();
            strBuilding.forEach(invoiceBuilding -> {
                Buildings building = buildingsRepository.findAllByBuildingid(invoiceBuilding.getBuildingid());
                buildings.add(building);
            });
            invoice.setBuildings(buildings);

            Integer type = invoice.getTypeOfMeterInvoice().getId();
            List<List<Flats>> flats = new ArrayList<>();
            AtomicReference<Integer> personNumber = new AtomicReference<>(0);
            AtomicReference<Integer> flatNumber = new AtomicReference<>(0);
            strBuilding.forEach(flatBuilding -> {
                Integer count = (flatsRepository.countFlatsByBuilding_Buildingid(flatBuilding.getBuildingid()));
                flatNumber.updateAndGet(v -> v + count);

                flats.add(flatsRepository.findFlatsByBuilding_Buildingid(flatBuilding.getBuildingid()));
                flats.forEach(building -> {
                    building.forEach(flat -> {
                        personNumber.updateAndGet(c -> c + flat.getNumberOfPerson());

                    });
                });
            });

            if (type == 4) {//TYPE_PERSON, which should be divided to number of persons that lives in those buildings
                invoice.setUnitPrice((double) Math.round((invoice.getInvoiceSum() / personNumber.get())*100.0)/100.0);
            } else {
                if (flatNumber != null) {
                    invoice.setUnitPrice((double) Math.round((invoice.getInvoiceSum() / flatNumber.get())*100.0)/100.0);
                }
            }

        }
        if (invoice.getMeter() != null) {
            Meters meter = new Meters();
            meter = metersRepository.findAllByMeterid(invoice.getMeter().getMeterId());
            invoice.setMeter(meter);
            invoice.setUnitPrice((double) Math.round((invoice.getInvoiceSum() / (invoice.getMeterDataCurrent() - invoice.getMeterDataPrevious()))*100.0)/100.0);
        }

        Status status = statusRepository.findByName(EStatus.STATUS_NEW);
        invoice.setStatus(status);

        invoicesService.addInvoice(invoice);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @PutMapping()
    public ResponseEntity<Invoices> updateInvoice(@RequestBody Invoices invoice) {
        Suppliers supplier = new Suppliers();
        supplier = suppliersRepository.findAllBySupplierid(invoice.getSupplier().getSupplierId());
        invoice.setSupplier(supplier);

        TypeOfMeterInvoice typeOfMeterInvoice = typeOfMeterInvoiceRepository.findAllById(invoice.getTypeOfMeterInvoice().getId());
        invoice.setTypeOfMeterInvoice(typeOfMeterInvoice);


        if (invoice.getBuildings() != null) {
            Set<Buildings> buildings = new HashSet<>();
            Set<Buildings> strBuilding = invoice.getBuildings();
            strBuilding.forEach(invoiceBuilding -> {
                Buildings building = buildingsRepository.findAllByBuildingid(invoiceBuilding.getBuildingid());
                buildings.add(building);
            });
            invoice.setBuildings(buildings);

            Integer type = invoice.getTypeOfMeterInvoice().getId();
            List<List<Flats>> flats = new ArrayList<>();
            AtomicReference<Integer> personNumber = new AtomicReference<>(0);
            AtomicReference<Integer> flatNumber = new AtomicReference<>(0);
            strBuilding.forEach(flatBuilding -> {
                Integer count = (flatsRepository.countFlatsByBuilding_Buildingid(flatBuilding.getBuildingid()));
                flatNumber.updateAndGet(v -> v + count);

                flats.add(flatsRepository.findFlatsByBuilding_Buildingid(flatBuilding.getBuildingid()));
                flats.forEach(building -> {
                    building.forEach(flat -> {
                        personNumber.updateAndGet(c -> c + flat.getNumberOfPerson());

                    });
                });
            });

            if (type == 4) {//TYPE_PERSON, which should be divided to number of persons that lives in those buildings
                invoice.setUnitPrice((double) Math.round((invoice.getInvoiceSum() / personNumber.get())*100.0)/100.0);
            } else {
                if (flatNumber != null) {
                    invoice.setUnitPrice((double) Math.round((invoice.getInvoiceSum() / flatNumber.get())*100.0)/100.0);
                }
            }

        }
        if (invoice.getMeter() != null) {
            Meters meter = new Meters();
            meter = metersRepository.findAllByMeterid(invoice.getMeter().getMeterId());
            invoice.setMeter(meter);
            invoice.setUnitPrice((double) Math.round((invoice.getInvoiceSum() / (invoice.getMeterDataCurrent() - invoice.getMeterDataPrevious()))*100.0)/100.0);
        }

        Invoices updateInvoice = invoicesService.updateInvoice(invoice);
        return new ResponseEntity<>(updateInvoice, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable("id") Integer id) throws InvoicesNotFoundException {
        invoicesService.deleteInvoice(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
