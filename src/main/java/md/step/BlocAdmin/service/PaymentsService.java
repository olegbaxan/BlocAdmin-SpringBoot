package md.step.BlocAdmin.service;

import md.step.BlocAdmin.exception.PaymentsNotFoundException;
import md.step.BlocAdmin.model.Flats;
import md.step.BlocAdmin.model.Payments;
import md.step.BlocAdmin.model.Person;
import md.step.BlocAdmin.repository.FlatsRepository;
import md.step.BlocAdmin.repository.PaymentsRepository;
import md.step.BlocAdmin.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentsService {
    private final PaymentsRepository paymentsRepository;
    private final PersonRepository personRepository;
    private final FlatsRepository flatsRepository;
    @Autowired
    public PaymentsService(PaymentsRepository paymentsRepository, PersonRepository personRepository,FlatsRepository flatsRepository) {
        this.paymentsRepository = paymentsRepository;
        this.personRepository = personRepository;
        this.flatsRepository = flatsRepository;

    }
    public Payments addPayment(Payments payment) {
        return paymentsRepository.save(payment);
    }

    public List<Payments> findAll() {
        return paymentsRepository.findAll();
    }

    public List<Person> findAllPersons() {
        return personRepository.findAll();
    }

    public List<Flats> findAllFlats() {
        return flatsRepository.findAll();
    }


    public Person getPersonByPaymentId(Integer id) {
        Optional<Payments> payments = paymentsRepository.findById(id);
        return payments.get().getPerson();
    }

    public Flats getFlatByPaymentId(Integer id) {
        Optional<Payments> payments = paymentsRepository.findById(id);
        return payments.get().getFlat();
    }

    public Payments updatePayment(Payments payment) {
        return paymentsRepository.save(payment);
    }


    public Payments findPaymentById(Integer id) throws PaymentsNotFoundException {
        return paymentsRepository.findById(id)
                .orElseThrow(() -> new PaymentsNotFoundException(id));
    }
    public List<Payments> findPaymentByPerson(Person person) throws PaymentsNotFoundException {
        return paymentsRepository.findAllByPerson(person);
    }

    public void deletePayment(Integer id) throws PaymentsNotFoundException {
        final Payments payment = this.paymentsRepository.findById(id).orElseThrow(() -> new PaymentsNotFoundException(id));
        paymentsRepository.delete(payment);
    }

    public Page<Payments> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.paymentsRepository.findAll(pageable);
    }

}
