package md.step.BlocAdmin.api;

import md.step.BlocAdmin.exception.InvoicesNotFoundException;
import md.step.BlocAdmin.message.ResponseFile;
import md.step.BlocAdmin.model.*;
import md.step.BlocAdmin.repository.*;
import md.step.BlocAdmin.security.services.EmailServiceImpl;
import md.step.BlocAdmin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/invoices")
public class InvoicesController {
    private final InvoicesService invoicesService;
    private final SuppliersService suppliersService;
    private final MetersService metersService;
    private final FlatsService flatsService;
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
    private EmailServiceImpl emailService;

    @Autowired
    public InvoicesController(InvoicesService invoicesService, SuppliersService suppliersService, MetersService metersService,
                              TypeOfMeterInvoiceRepository typeOfMeterInvoiceRepository, FileStorageService fileStorageService,
                              StatusRepository statusRepository, BuildingsRepository buildingsRepository, FlatsService flatsService,
                              PersonRepository personRepository) {
        this.invoicesService = invoicesService;
        this.suppliersService = suppliersService;
        this.metersService = metersService;
        this.personRepository = personRepository;
        this.buildingsRepository = buildingsRepository;
        this.typeOfMeterInvoiceRepository = typeOfMeterInvoiceRepository;
        this.fileStorageService = fileStorageService;
        this.statusRepository = statusRepository;
        this.flatsService = flatsService;
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllInvoices(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Invoices> invoices = new ArrayList<Invoices>();
            Pageable paging = PageRequest.of(page, size, Sort.by("emittedDate").descending());


            Page<Invoices> pageInvoices;
            if (title == null) {
                pageInvoices = invoicesRepository.findAll(paging);
            } else {
                pageInvoices = invoicesRepository.findAllDistinctInvoicesByInvoiceNumberStartingWithIgnoreCaseOrSupplier_SupplierNameContainingIgnoreCaseOrBuildings_Address_CityStartingWithIgnoreCaseOrBuildings_Address_RaionStartingWithIgnoreCaseOrBuildings_Address_StreetStartingWithIgnoreCase(title, title, title, title, title, paging);

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

    @GetMapping("invoiceno/{invoice}")
    public Boolean checkInvoiceNo(@PathVariable("invoice") String invoice) {
        return this.invoicesRepository.existsInvoicesByInvoiceNumber(invoice);
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("suppliers")
    public ResponseEntity<List<Suppliers>> getSuppliers() {
        List<Suppliers> suppliers = invoicesService.findAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("buildings")
    public ResponseEntity<List<Buildings>> getBuildings() {
        List<Buildings> buildings = invoicesService.findAllBuildings();
        return ResponseEntity.ok(buildings);
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("meters")
    public ResponseEntity<List<Meters>> getMeters() {
        List<Meters> meters = invoicesService.findAllMeters();
        return ResponseEntity.ok(meters);
    }

    @GetMapping("file/{id}")
    public ResponseEntity<Map<String, Object>> getFile(@PathVariable("id") String name) {
        Map<String, Object> response = new HashMap<>();
        if (name.length() > 0) {
            FileDB invoiceFile = invoicesService.getFileByName(name);
            System.out.println("Invoicefile=" + invoiceFile);
            System.out.println("Invoicename=" + name);
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
            System.out.println("FileInfo=" + files);
            response.put("invoiceFile", invoiceFile);
            response.put("fileInfo", files);
        }


//            return ResponseEntity.status(HttpStatus.OK).body(files);


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("typeofmeterandinvoice")
    public ResponseEntity<List<TypeOfMeterInvoice>> getTypeOfMeterInvoice() {
        List<TypeOfMeterInvoice> type = invoicesService.findAllTyepOfMeterAndInvoice();
        return ResponseEntity.ok(type);
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("/{id}")
    public ResponseEntity<Invoices> getInvoiceById(@PathVariable("id") Integer id) throws InvoicesNotFoundException {
        Invoices invoice = invoicesService.findInvoiceById(id);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @GetMapping("personinvoices/{id}")
    public ResponseEntity<List<Invoices>> getInvoiceByPersonId(@PathVariable("id") Integer id) throws InvoicesNotFoundException {
        Person person = personRepository.findAllByPersonid(id);
        System.out.println("Person Invoice =" + person);
        List<Invoices> invoice = invoicesRepository.findFirst10InvoicesByMeter_PersonOrMeter_Flat_PersonOrderByEmittedDateDesc(person, person);

        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @GetMapping("newpersoninvoices")
    public ResponseEntity<List<Invoices>> getNewInvoicePerson() {
        TypeOfMeterInvoice typeOfMeterInvoice = typeOfMeterInvoiceRepository.findAllById(3);//TYPE_FLATS
        Status statusNew = statusRepository.findByName(EStatus.STATUS_NEW);
        Status statusSend = statusRepository.findByName(EStatus.STATUS_SENDINVOICE);
        List<Invoices> invoice = invoicesRepository.findInvoicesByTypeOfMeterInvoiceAndStatusOrStatus(typeOfMeterInvoice, statusNew, statusSend);

        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @GetMapping("newsupplierinvoices")
    public ResponseEntity<List<Invoices>> getNewInvoiceSupplier() {
        TypeOfMeterInvoice typeOfMeterInvoice = typeOfMeterInvoiceRepository.findAllById(1);//TYPE_BUILDING
        Status statusNew = statusRepository.findByName(EStatus.STATUS_NEW);

        List<Invoices> invoice = invoicesRepository.findInvoicesByTypeOfMeterInvoiceAndStatusOrStatus(typeOfMeterInvoice, statusNew, statusNew);

        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @GetMapping("supplierinvoices/{id}")
    public ResponseEntity<List<Invoices>> getInvoiceBySupplier(@PathVariable("id") Integer id) {
        Person person = personRepository.findAllByPersonid(id);
        List<Invoices> invoice = invoicesRepository.findInvoicesByTypeOfMeterInvoiceName(ETypeOfMeterInvoice.TYPE_BUILDING);//TYPE_BUILDING
        List<Flats> flats = flatsRepository.findFlatsByPerson(person);
        List<Invoices> suppInvoice = new ArrayList<>();
        for (int j = 0; j < flats.size(); j++) {
            for (int i = 0; i < invoice.size(); i++) {
                if (flats.get(j).getBuilding().getAddress().getAddressid() == invoice.get(i).getMeter().getBuilding().getAddress().getAddressid()) {
                    suppInvoice.add(invoice.get(i));
                }
            }
            System.out.println("Supp inv = " + suppInvoice);
        }
        return new ResponseEntity<>(suppInvoice, HttpStatus.OK);
    }

    @GetMapping("meters/{id}")
    public ResponseEntity<List<Meters>> getMetersByBuilding(@PathVariable("id") Integer id) {
        List<Meters> meters = metersRepository.findMetersByBuilding_Buildingid(id);
        return new ResponseEntity<>(meters, HttpStatus.OK);
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
    @PostMapping()
    public ResponseEntity<Invoices> addInvoice(@RequestBody Invoices invoice) {

        //Get the type of Invoice:1-TYPE_BUILDING & 2- TYPE_LADDER, 3-TYPE_FLATS, 4-TYPE_PERSON
        Integer type = invoice.getTypeOfMeterInvoice().getId();
        System.out.println("InvoiceType = " + type);
        TypeOfMeterInvoice typeOfMeterInvoice = typeOfMeterInvoiceRepository.findAllById(type);
        invoice.setTypeOfMeterInvoice(typeOfMeterInvoice);


        Suppliers supplier = new Suppliers();
        supplier = suppliersRepository.findAllBySupplierid(invoice.getSupplier().getSupplierId());
        invoice.setSupplier(supplier);

        //Set emitted date if it is not set
        if (invoice.getEmittedDate() == null) {
            invoice.setEmittedDate(LocalDate.now());
        }

        Set<Person> persons = new HashSet<>();

        if (invoice.getBuildings() != null) {
            Set<Buildings> buildings = new HashSet<>();
            Set<Buildings> strBuilding = invoice.getBuildings();
            strBuilding.forEach(invoiceBuilding -> {
                Buildings building = buildingsRepository.findAllByBuildingid(invoiceBuilding.getBuildingid());
                buildings.add(building);
            });
            invoice.setBuildings(buildings);
            Integer totalFlats = 0;
            Integer totalPersons = 0;
            List<Flats> invoiceFlats = new ArrayList<>();
            List<Person> flatPersons = new ArrayList<>();
            List<Buildings> arrayBuild = new ArrayList<>(invoice.getBuildings());
            //count flats from selected buildings
            for (int i = 0; i < arrayBuild.size(); i++) {
                totalFlats = totalFlats + (flatsRepository.countFlatsByBuilding_Buildingid(arrayBuild.get(i).getBuildingid()));
                System.out.println("Total Flats=" + totalFlats);
                List<Flats> buildingFlats = flatsRepository.findFlatsByBuilding_Buildingid(arrayBuild.get(i).getBuildingid());
                for (int j = 0; j < buildingFlats.size(); j++) {
                    invoiceFlats.add(buildingFlats.get(j));
                    System.out.println("Total Invoice Flats=" + buildingFlats.get(j));
                    //Count persons that lives in flats
                    totalPersons = totalPersons + buildingFlats.get(j).getNumberOfPerson();
                    List<Person> flatPerson = new ArrayList<>(buildingFlats.get(j).getPerson());
                    System.out.println("Total Person = " + totalPersons);

                    for (int p = 0; p < flatPerson.size(); p++) {
                        flatPersons.add(flatPerson.get(p));
                        System.out.println("Flat Person = " + flatPerson.get(p));
                    }
                }
            }

            //Set default status for all invoices
            Status status = statusRepository.findByName(EStatus.STATUS_NEW);
            invoice.setStatus(status);

            //Depending of type of Invoice we should
            //1. For type = TYPE_PERSON we should:
            if (type == 4) {//TYPE_PERSON, which should be divided to number of persons that lives in those buildings
                //Set unit price for Invoice depending of number of person that lives in flats
                invoice.setUnitPrice((double) Math.round((invoice.getInvoiceSum() / totalPersons) * 100.0) / 100.0);

                //for every flat to create an invoice
                invoiceFlats.forEach(flat -> {
                    Invoices flatInvoice = new Invoices();
                    flatInvoice.setInvoiceNumber(invoice.getInvoiceNumber().concat(String.valueOf("/" + LocalDate.now().getYear()).concat("/" + LocalDate.now().getDayOfMonth()).concat("/" + flat.getFlatNumber()).concat(String.valueOf("/" + flat.getFlatid()))));
                    flatInvoice.setTypeOfMeterInvoice(invoice.getTypeOfMeterInvoice());
                    flatInvoice.setBuildings(invoice.getBuildings());
                    flatInvoice.setStatus(statusRepository.findByName(EStatus.STATUS_SENDINVOICE));//PAYED
                    flatInvoice.setEmittedDate(invoice.getEmittedDate());
                    flatInvoice.setUnitPrice(invoice.getUnitPrice());
                    flatInvoice.setSupplier(invoice.getSupplier());
                    flatInvoice.setInvoiceSum(invoice.getUnitPrice() * flat.getNumberOfPerson());
                    flatInvoice.setPayTill(invoice.getPayTill());

                    flat.setWallet(flat.getWallet() - flatInvoice.getInvoiceSum());
                    flatsService.updateFlat(flat);
                    invoicesService.addInvoice(flatInvoice);

                    //Send mail to Person of the flat
                    flat.getPerson().forEach(person -> {
                        SimpleMailMessage InvoiceEmail = new SimpleMailMessage();
                        InvoiceEmail.setFrom("oleg.baxan.test@gmail.com");
                        InvoiceEmail.setTo(person.getEmail());
                        InvoiceEmail.setSubject("Information about a new invoice");
                        InvoiceEmail.setText(
                                "Hello, " + person.getName() + " " + person.getSurname() +
                                        "\n\nA new invoice, with number: " + flatInvoice.getInvoiceNumber() + ", was generated for your flat: " +
                                        flat.getFlatNumber() +
                                        ".\n - Supplier: " + flatInvoice.getSupplier().getSupplierName() +
                                        "\n - Meter Previous: " + flatInvoice.getMeterDataPrevious() +
                                        "\n - Meter Current: " + flatInvoice.getMeterDataCurrent() +
                                        "\n - Pay till: " + flatInvoice.getPayTill() +
                                        "\n - Emitted Date: " + flatInvoice.getEmittedDate() +
                                        "\n - Total sum: " + flatInvoice.getInvoiceSum()

                        );
                        emailService.sendEmail(InvoiceEmail);
                    });
                });
                invoice.setTypeOfMeterInvoice(typeOfMeterInvoiceRepository.findAllById(1));//Set TYPE_BUILDING
                invoice.setStatus(statusRepository.findByName(EStatus.STATUS_PAYED));
//                invoicesService.addInvoice(invoice);
            } else if (type == 1 || type == 2) {
                if (totalFlats != 0) {
                    invoice.setTypeOfMeterInvoice(typeOfMeterInvoiceRepository.findAllById(1));//Set TYPE_BUILDING
                    invoice.setUnitPrice((double) Math.round((invoice.getInvoiceSum() / (invoice.getMeterDataCurrent() - invoice.getMeterDataPrevious())) * 100.0) / 100.0);
//                    invoicesService.addInvoice(invoice);
                }
            } else if (type == 3) {
                invoice.getMeter().getFlat().setWallet(invoice.getMeter().getFlat().getWallet() - invoice.getInvoiceSum());
                flatsService.updateFlat(invoice.getMeter().getFlat());

                invoice.getMeter().getFlat().getPerson().forEach(person -> {
                    SimpleMailMessage InvoiceEmail = new SimpleMailMessage();
                    InvoiceEmail.setFrom("oleg.baxan.test@gmail.com");
                    InvoiceEmail.setTo(person.getEmail());
                    InvoiceEmail.setSubject("Information about a new invoice");
                    InvoiceEmail.setText(
                            "Hello, " + person.getName() + " " + person.getSurname() +
                                    "\n\nA new invoice, with number: " + invoice.getInvoiceNumber() + ", was generated for your flat: " +
                                    invoice.getMeter().getFlat().getFlatNumber() +
                                    ".\n - Supplier: " + invoice.getSupplier().getSupplierName() +
                                    "\n - Meter Previous: " + invoice.getMeterDataPrevious() +
                                    "\n - Meter Current: " + invoice.getMeterDataCurrent() +
                                    "\n - Pay till: " + invoice.getPayTill() +
                                    "\n - Emitted Date: " + invoice.getEmittedDate() +
                                    "\n - Total sum: " + invoice.getInvoiceSum()

                    );

                    emailService.sendEmail(InvoiceEmail);
                });
                invoice.setStatus(statusRepository.findByName(EStatus.STATUS_PAYED));
            }

            invoicesService.addInvoice(invoice);
        }
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
    @PutMapping()
    public ResponseEntity<Invoices> updateInvoice(@RequestBody Invoices invoice) {
        //Get the type of Invoice:1-TYPE_BUILDING & 2- TYPE_LADDER, 3-TYPE_FLATS, 4-TYPE_PERSON
        Integer type = invoice.getTypeOfMeterInvoice().getId();
        TypeOfMeterInvoice typeOfMeterInvoice = typeOfMeterInvoiceRepository.findAllById(type);
        System.out.println("Status = " + invoice.getStatus().getName() + " ,type = " + type);
        invoice.setTypeOfMeterInvoice(typeOfMeterInvoice);


        Suppliers supplier = new Suppliers();
        supplier = suppliersRepository.findAllBySupplierid(invoice.getSupplier().getSupplierId());
        invoice.setSupplier(supplier);


        Set<Person> persons = new HashSet<>();

        if (invoice.getBuildings() != null) {
            Set<Buildings> buildings = new HashSet<>();
            Set<Buildings> strBuilding = invoice.getBuildings();
            strBuilding.forEach(invoiceBuilding -> {
                Buildings building = buildingsRepository.findAllByBuildingid(invoiceBuilding.getBuildingid());
                buildings.add(building);
            });
            invoice.setBuildings(buildings);
            Integer totalFlats = 0;
            Integer totalPersons = 0;
            List<Flats> invoiceFlats = new ArrayList<>();
            List<Person> flatPersons = new ArrayList<>();
            List<Buildings> arrayBuild = new ArrayList<>(invoice.getBuildings());
            //count flats from selected buildings
            for (int i = 0; i < arrayBuild.size(); i++) {
                totalFlats = totalFlats + (flatsRepository.countFlatsByBuilding_Buildingid(arrayBuild.get(i).getBuildingid()));
                System.out.println("Total Flats=" + totalFlats);
                List<Flats> buildingFlats = flatsRepository.findFlatsByBuilding_Buildingid(arrayBuild.get(i).getBuildingid());
                for (int j = 0; j < buildingFlats.size(); j++) {
                    invoiceFlats.add(buildingFlats.get(j));
                    System.out.println("Total Invoice Flats=" + buildingFlats.get(j));
                    //Count persons that lives in flats
                    totalPersons = totalPersons + buildingFlats.get(j).getNumberOfPerson();
                    List<Person> flatPerson = new ArrayList<>(buildingFlats.get(j).getPerson());
                    System.out.println("Total Person = " + totalPersons);

                    for (int p = 0; p < flatPerson.size(); p++) {
                        flatPersons.add(flatPerson.get(p));
                        System.out.println("Flat Person = " + flatPerson.get(p));
                    }
                }
            }
            if (invoice.getStatus() == null) {
                Status status = statusRepository.findByName(EStatus.STATUS_NEW);
                invoice.setStatus(status);
            }
            if (totalFlats != 0) {
                invoice.setUnitPrice((double) Math.round((invoice.getInvoiceSum() / (invoice.getMeterDataCurrent() - invoice.getMeterDataPrevious())) * 100.0) / 100.0);
//                    invoice.setStatus(statusRepository.findByName(EStatus.STATUS_NEW));
            }


            //adding one day to the localdate
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            if (invoice.getDateOfPay() != null) {
                if (tomorrow.isAfter(invoice.getDateOfPay())) {
                    invoice.setStatus(statusRepository.findByName(EStatus.STATUS_CLOSED));
                } else {
                    invoice.setDateOfPay(null);
                }
            }

        }
        System.out.println("Update Invoice = " + invoice);
        invoicesService.updateInvoice(invoice);
        return new ResponseEntity<>(invoice, HttpStatus.OK);

    }


    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable("id") Integer id) throws InvoicesNotFoundException {
        Invoices invoice = invoicesRepository.findInvoiceByInvoiceId(id);
        Set<Flats> invoiceFlats = new HashSet<>();
        List<Buildings> arrayBuild = new ArrayList<>(invoice.getBuildings());
        String invoiceName = invoice.getInvoiceNumber().concat("/");
        List<Invoices> allChildInvoices = invoicesRepository.findInvoicesByInvoiceNumberStartingWith(invoiceName);
        System.out.println("allChildInvoices = " + allChildInvoices);

        if (allChildInvoices.size() > 0) {
            for (int i = 0; i < allChildInvoices.size(); i++) {
                if (allChildInvoices.get(i).getMeter() != null) {
                    allChildInvoices.get(i).getMeter().getFlat().setWallet(allChildInvoices.get(i).getMeter().getFlat().getWallet()
                            + allChildInvoices.get(i).getInvoiceSum());
                    flatsService.updateFlat(allChildInvoices.get(i).getMeter().getFlat());
                } else {
                    if (allChildInvoices.get(i).getBuildings().size() > 0) {
                        for (int b = 0; b < arrayBuild.size(); b++) {
                            List<Flats> buildingFlats = flatsRepository.findFlatsByBuilding_Buildingid(arrayBuild.get(b).getBuildingid());
                            System.out.println("buildingFlats = " + buildingFlats);
                            for (int j = 0; j < buildingFlats.size(); j++) {
                                invoiceFlats.add(buildingFlats.get(j));
                            }
                        }
                    }
                }
                System.out.println("Invoice to delete = " + allChildInvoices.get(i).getInvoiceNumber());
                invoicesService.deleteInvoice(allChildInvoices.get(i).getInvoiceId());
            }
            if (invoiceFlats.size() > 0) {
                invoiceFlats.forEach(flat -> {
                    System.out.println("Flat = Flat = " + flat.getFlatNumber() + ", ID: " + flat.getFlatid());
                    flat.setWallet(flat.getWallet() + (invoice.getUnitPrice() * flat.getNumberOfPerson()));
                    flatsService.updateFlat(flat);
                });
            }
        }

        invoicesService.deleteInvoice(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

