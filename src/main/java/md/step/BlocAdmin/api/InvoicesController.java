package md.step.BlocAdmin.api;

import md.step.BlocAdmin.exception.InvoicesNotFoundException;
import md.step.BlocAdmin.model.Invoices;
import md.step.BlocAdmin.model.Meters;
import md.step.BlocAdmin.model.Suppliers;
import md.step.BlocAdmin.model.TypeOfMeterAndInvoice;
import md.step.BlocAdmin.repository.InvoicesRepository;
import md.step.BlocAdmin.repository.MetersRepository;
import md.step.BlocAdmin.repository.SuppliersRepository;
import md.step.BlocAdmin.repository.TypeOfMeterAndInvoiceRepository;
import md.step.BlocAdmin.service.InvoicesService;
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
@RequestMapping("/api/v1/invoices")
public class InvoicesController {
    private final InvoicesService invoicesService;
    private final SuppliersService suppliersService;
    private final MetersService metersService;

    @Autowired
    private InvoicesRepository invoicesRepository;
    @Autowired
    private SuppliersRepository suppliersRepository;
    @Autowired
    private MetersRepository metersRepository;
    @Autowired
    private TypeOfMeterAndInvoiceRepository typeOfMeterAndInvoiceRepository;

    @Autowired
    public InvoicesController(InvoicesService invoicesService, SuppliersService suppliersService, MetersService metersService,
                              TypeOfMeterAndInvoiceRepository typeOfMeterAndInvoiceRepository) {
        this.invoicesService = invoicesService;
        this.suppliersService = suppliersService;
        this.metersService = metersService;
    }
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
    @GetMapping("supplier")
    public ResponseEntity<List<Suppliers>> getSuppliers() {
        List<Suppliers> suppliers = invoicesService.findAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }
    @GetMapping("meters")
    public ResponseEntity<List<Meters>> getMeters() {
        List<Meters> meters = invoicesService.findAllMeters();
        return ResponseEntity.ok(meters);
    }
    @GetMapping("typeofmeterandinvoice")
    public ResponseEntity<List<TypeOfMeterAndInvoice>> getTypeOfMeterAndInvoice() {
        List<TypeOfMeterAndInvoice> type = invoicesService.findAllTyepOfMeterAndInvoice();
        return ResponseEntity.ok(type);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoices> getInvoiceById(@PathVariable("id") Integer id) throws InvoicesNotFoundException {
        Invoices invoice = invoicesService.findInvoiceById(id);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Invoices> addInvoice(@RequestBody Invoices invoice) {
        Suppliers supplier = new Suppliers();
        supplier=suppliersRepository.findAllBySupplierid(invoice.getSupplier().getSupplierId());
        invoice.setSupplier(supplier);

        Meters meter=new Meters();
        meter=metersRepository.findAllByMeterid(invoice.getMeter().getMeterId());
        invoice.setMeter(meter);

        TypeOfMeterAndInvoice typeOfMeterAndInvoice = new TypeOfMeterAndInvoice();
        typeOfMeterAndInvoice= typeOfMeterAndInvoiceRepository.findAllById(invoice.getTypeOfMeterAndInvoice().getId());
        meter.setTypeOfMeterAndInvoice(typeOfMeterAndInvoice);

        invoicesService.addInvoice(invoice);

        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Invoices> updateInvoice(@RequestBody Invoices invoice) {
        Suppliers supplier = new Suppliers();
        supplier=suppliersRepository.findAllBySupplierid(invoice.getSupplier().getSupplierId());
        invoice.setSupplier(supplier);

        Meters meter=new Meters();
        meter=metersRepository.findAllByMeterid(invoice.getMeter().getMeterId());
        invoice.setMeter(meter);

        Invoices updateInvoice = invoicesService.updateInvoice(invoice);
        return new ResponseEntity<>(updateInvoice, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable("id") Integer id) throws InvoicesNotFoundException {
        invoicesService.deleteInvoice(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
