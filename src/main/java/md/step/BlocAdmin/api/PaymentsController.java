package md.step.BlocAdmin.api;

import md.step.BlocAdmin.exception.FlatsNotFoundException;
import md.step.BlocAdmin.exception.InvoicesNotFoundException;
import md.step.BlocAdmin.exception.PaymentsNotFoundException;
import md.step.BlocAdmin.model.*;
import md.step.BlocAdmin.repository.*;
import md.step.BlocAdmin.security.services.EmailServiceImpl;
import md.step.BlocAdmin.service.FlatsService;
import md.step.BlocAdmin.service.InvoicesService;
import md.step.BlocAdmin.service.PaymentsService;
import md.step.BlocAdmin.service.PersonService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(maxAge = 3600, allowCredentials = "true",origins = "https://blocadmin-angularui.herokuapp.com/")
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentsController {
    private final PaymentsService paymentsService;
    private final PersonService personService;
    private final FlatsService flatsService;

    @Autowired
    private PaymentsRepository paymentsRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FlatsRepository flatsRepository;
    @Autowired
    private InvoicesService invoicesService;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    public PaymentsController(PaymentsService paymentsService, PersonService personService, FlatsService flatsService,
                              InvoicesService invoicesService,StatusRepository statusRepository,RoleRepository roleRepository) {
        this.paymentsService = paymentsService;
        this.personService = personService;
        this.flatsService = flatsService;
        this.invoicesService=invoicesService;
        this.statusRepository=statusRepository;
        this.roleRepository=roleRepository;
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllPayments(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Payments> payments = new ArrayList<Payments>();
            Pageable paging = PageRequest.of(page, size, Sort.by("paymentid").descending());

            Page<Payments> pagePayments;
            if (title == null) {
                pagePayments = paymentsRepository.findAll(paging);
            } else {
                pagePayments = paymentsRepository.findDistinctByPerson_NameContainingIgnoreCaseOrPerson_SurnameContainingIgnoreCase(title,title,paging);
            }

            payments = pagePayments.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("payments", payments);
            response.put("currentPage", pagePayments.getNumber());
            response.put("totalItems", pagePayments.getTotalElements());
            response.put("totalPages", pagePayments.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("persons")
    public ResponseEntity<List<Person>> getPersons() {
        List<Person> persons = paymentsService.findAllPersons();
        persons = persons.stream().filter(p -> p.getRoles().contains(roleRepository.findByName(ERole.ROLE_USER).get())).collect(Collectors.toList());
        return ResponseEntity.ok(persons);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("flats")
    public ResponseEntity<List<Flats>> getFlats() {
        List<Flats> flats = paymentsService.findAllFlats();
        return ResponseEntity.ok(flats);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("personflats/{id}")
    public ResponseEntity<List<Flats>> getPersonFlats(@PathVariable("id") Integer id) throws  FlatsNotFoundException {
        List<Flats> personFlats = flatsService.findFlatsByPersonId(id);
        return ResponseEntity.ok(personFlats);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("flatinvoices/{id}")
    public ResponseEntity<List<Invoices>> getFlatInvoices(@PathVariable("id") Integer id) throws InvoicesNotFoundException {
        //Status status=statusRepository.findAllByName("STATUS_SENDINVOICE");// for PROD
        Status status=statusRepository.findByName(EStatus.STATUS_NEW);// for TEST
        List<Invoices> flatInvoices = invoicesService.getInvoicesByFlatId(id,status);
        return ResponseEntity.ok(flatInvoices);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("/{id}")
    public ResponseEntity<Payments> getPaymentById(@PathVariable("id") Integer id) throws PaymentsNotFoundException {
        Payments payment = paymentsService.findPaymentById(id);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }
    @GetMapping("/person/{id}")
    public ResponseEntity<List<Payments>> getPaymentByPersonId(@PathVariable("id") Integer id) throws PaymentsNotFoundException {
        Person person = personRepository.findAllByPersonid(id);
        List<Payments> payment = paymentsService.findPaymentByPerson(person);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @PostMapping()
    public ResponseEntity<List<Payments>> addPayments(@RequestBody List<Payments> payments) {
        for (int i=0;i<payments.size();i++){

            if (payments.get(i).getPayDate()==null){
                payments.get(i).setPayDate(LocalDate.now());
            }

            Person person = new Person();
            person=personRepository.findAllByPersonid(payments.get(i).getPerson().getPersonid());
            payments.get(i).setPerson(person);

            Flats flat=new Flats();
            flat=flatsRepository.findAllByFlatid(payments.get(i).getFlat().getFlatid());
            if(payments.get(i).getSum()>0) {
                flat.setWallet(flat.getWallet()+payments.get(i).getSum());
                payments.get(i).setFlat(flat);
                paymentsService.addPayment(payments.get(i));
            }

            // Email message
                SimpleMailMessage PaymentEmail = new SimpleMailMessage();
            PaymentEmail.setFrom("oleg.baxan.test@gmail.com");
            PaymentEmail.setTo(person.getEmail());
            PaymentEmail.setSubject("Information about a new invoice");
            PaymentEmail.setText("Hello, "+ person.getName()+ " "+ person.getSurname()+
                        "\n\nA new payment was made for your flat: "+ flat.getFlatNumber()+
                        ".\n - Payment: "+payments.get(i).getSum()+
                        "\n - Wallet: "+flat.getWallet()

                );

                emailService.sendEmail(PaymentEmail);


            //After payment check if Flat has invoices that needs to be paid
            Status status=statusRepository.findByName(EStatus.STATUS_NEW);// for TEST

            List<Invoices> flatInvoices = invoicesService.getInvoicesByFlatId(flat.getFlatid(),status);
            if(flatInvoices.size()>0){
                for (int j=0;j<flatInvoices.size();j++){
                    System.out.println("InvoiceID = "+ flatInvoices.get(j).getInvoiceId());
                    System.out.println("flat.getWallet() = "+ flat.getWallet());
                    flat.setWallet(flat.getWallet()-flatInvoices.get(j).getInvoiceSum());
                    flatInvoices.get(j).setStatus(statusRepository.findByName(EStatus.STATUS_PAYED));
                    invoicesService.updateInvoice(flatInvoices.get(j));
                }
                flatsService.updateFlat(flat);
            }



        }
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @PutMapping()
    public ResponseEntity<Payments> updateInvoice(@RequestBody Payments payment) {
        if (payment.getPayDate()==null){
            payment.setPayDate(LocalDate.now());
        }

        Person person = new Person();
        person=personRepository.findAllByPersonid(payment.getPerson().getPersonid());
        payment.setPerson(person);

        Flats flat=new Flats();
        flat=flatsRepository.findAllByFlatid(payment.getFlat().getFlatid());
        if(payment.getSum()>0) {
            flat.setWallet(flat.getWallet()+payment.getSum());
            payment.setFlat(flat);
            paymentsService.addPayment(payment);
        }
        payment.setFlat(flat);

        Payments updatePayment = paymentsService.updatePayment(payment);
        return new ResponseEntity<>(updatePayment, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable("id") Integer id) throws PaymentsNotFoundException {
        paymentsService.deletePayment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
