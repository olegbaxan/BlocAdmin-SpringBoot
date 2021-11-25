package md.step.BlocAdmin.api;

import md.step.BlocAdmin.exception.WithdrawnNotFoundException;
import md.step.BlocAdmin.model.Flats;
import md.step.BlocAdmin.model.Invoices;
import md.step.BlocAdmin.model.Withdrawn;
import md.step.BlocAdmin.repository.FlatsRepository;
import md.step.BlocAdmin.repository.InvoicesRepository;
import md.step.BlocAdmin.repository.WithdrawnRepository;
import md.step.BlocAdmin.service.FlatsService;
import md.step.BlocAdmin.service.InvoicesService;
import md.step.BlocAdmin.service.WithdrawnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/withdrawn")
public class WithdrawnController {
    private final WithdrawnService withdrawnService;
    private final InvoicesService invoicesService;
    private final FlatsService flatsService;

    @Autowired
    private WithdrawnRepository withdrawnRepository;
    @Autowired
    private InvoicesRepository invoicesRepository;
    @Autowired
    private FlatsRepository flatsRepository;

    @Autowired
    public WithdrawnController(WithdrawnService withdrawnService, InvoicesService invoicesService, FlatsService flatsService) {
        this.invoicesService = invoicesService;
        this.withdrawnService = withdrawnService;
        this.flatsService = flatsService;
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllPayments(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Withdrawn> withdrawns = new ArrayList<Withdrawn>();
            Pageable paging = PageRequest.of(page, size);

            Page<Withdrawn> pageWithdrawns;
            if (title == null) {
                pageWithdrawns = withdrawnRepository.findAll(paging);
            } else {
                pageWithdrawns = withdrawnRepository.findAll(paging);
            }

            withdrawns = pageWithdrawns.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("invoices", withdrawns);
            response.put("currentPage", pageWithdrawns.getNumber());
            response.put("totalItems", pageWithdrawns.getTotalElements());
            response.put("totalPages", pageWithdrawns.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("invoices")
    public ResponseEntity<List<Invoices>> getInvoices() {
        List<Invoices> invoices = withdrawnService.findAllInvoices();
        return ResponseEntity.ok(invoices);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("flats")
    public ResponseEntity<List<Flats>> getFlats() {
        List<Flats> flats = withdrawnService.findAllFlats();
        return ResponseEntity.ok(flats);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("/{id}")
    public ResponseEntity<Withdrawn> getWithdrawnById(@PathVariable("id") Integer id) throws WithdrawnNotFoundException {
        Withdrawn withdrawn = withdrawnService.findWithdrawnById(id);
        return new ResponseEntity<>(withdrawn, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @PostMapping()
    public ResponseEntity<Withdrawn> addWithdrawn(@RequestBody Withdrawn withdrawn) {
        if (withdrawn.getWithdrawnDate()==null){
            withdrawn.setWithdrawnDate(LocalDate.now());
        }

        Invoices invoice = new Invoices();
        invoice=invoicesRepository.findInvoiceByInvoiceId(withdrawn.getInvoice().getInvoiceId());
        withdrawn.setInvoice(invoice);

        Flats flat=new Flats();
        flat=flatsRepository.findAllByFlatid(withdrawn.getFlat().getFlatid());
        withdrawn.setFlat(flat);


        withdrawnService.addWithdrawn(withdrawn);

        return new ResponseEntity<>(withdrawn, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @PutMapping()
    public ResponseEntity<Withdrawn> updateWithdrawn(@RequestBody Withdrawn withdrawn) {
        if (withdrawn.getWithdrawnDate()==null){
            withdrawn.setWithdrawnDate(LocalDate.now());
        }

        Invoices invoice = new Invoices();
        invoice=invoicesRepository.findInvoiceByInvoiceId(withdrawn.getInvoice().getInvoiceId());
        withdrawn.setInvoice(invoice);

        Flats flat=new Flats();
        flat=flatsRepository.findAllByFlatid(withdrawn.getFlat().getFlatid());
        withdrawn.setFlat(flat);

        Withdrawn updateWithdrawn = withdrawnService.updateWithdrawn(withdrawn);
        return new ResponseEntity<>(updateWithdrawn, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable("id") Integer id) throws WithdrawnNotFoundException {
        withdrawnService.deleteWithdrawn(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
