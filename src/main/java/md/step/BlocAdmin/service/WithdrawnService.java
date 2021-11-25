package md.step.BlocAdmin.service;

import md.step.BlocAdmin.exception.WithdrawnNotFoundException;
import md.step.BlocAdmin.model.Flats;
import md.step.BlocAdmin.model.Invoices;
import md.step.BlocAdmin.model.Withdrawn;
import md.step.BlocAdmin.repository.FlatsRepository;
import md.step.BlocAdmin.repository.InvoicesRepository;
import md.step.BlocAdmin.repository.WithdrawnRepository;
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
public class WithdrawnService {
    private final WithdrawnRepository withdrawnRepository;
    private final InvoicesRepository invoicesRepository;
    private final FlatsRepository flatsRepository;

    @Autowired
    public WithdrawnService(WithdrawnRepository withdrawnRepository, InvoicesRepository invoicesRepository,FlatsRepository flatsRepository) {
        this.withdrawnRepository = withdrawnRepository;
        this.invoicesRepository = invoicesRepository;
        this.flatsRepository = flatsRepository;

    }
    public Withdrawn addWithdrawn(Withdrawn withdrawn) {
        return withdrawnRepository.save(withdrawn);
    }

    public List<Withdrawn> findAll() {
        return withdrawnRepository.findAll();
    }

    public List<Invoices> findAllInvoices() {
        return invoicesRepository.findAll();
    }
    public List<Flats> findAllFlats() {
        return flatsRepository.findAll();
    }

    public Invoices getInvoiceByWithdrawnId(Integer id) {
        Optional<Withdrawn> withdrawn = withdrawnRepository.findById(id);
        return withdrawn.get().getInvoice();
    }
    public Flats getFlatByWithdrawnId(Integer id) {
        Optional<Withdrawn> withdrawn = withdrawnRepository.findById(id);
        return withdrawn.get().getFlat();
    }

    public Withdrawn updateWithdrawn(Withdrawn withdrawn) {
        return withdrawnRepository.save(withdrawn);
    }


    public Withdrawn findWithdrawnById(Integer id) throws WithdrawnNotFoundException {
        return withdrawnRepository.findById(id)
                .orElseThrow(() -> new WithdrawnNotFoundException(id));
    }

    public void deleteWithdrawn(Integer id) throws WithdrawnNotFoundException {
        final Withdrawn withdrawn = this.withdrawnRepository.findById(id).orElseThrow(() -> new WithdrawnNotFoundException(id));
        withdrawnRepository.delete(withdrawn);
    }

    public Page<Withdrawn> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.withdrawnRepository.findAll(pageable);
    }
}
