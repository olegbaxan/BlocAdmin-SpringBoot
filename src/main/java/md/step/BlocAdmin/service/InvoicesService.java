package md.step.BlocAdmin.service;

import md.step.BlocAdmin.exception.InvoicesNotFoundException;
import md.step.BlocAdmin.model.Invoices;
import md.step.BlocAdmin.model.Meters;
import md.step.BlocAdmin.model.Suppliers;
import md.step.BlocAdmin.model.TypeOfMeterAndInvoice;
import md.step.BlocAdmin.repository.InvoicesRepository;
import md.step.BlocAdmin.repository.MetersRepository;
import md.step.BlocAdmin.repository.SuppliersRepository;
import md.step.BlocAdmin.repository.TypeOfMeterAndInvoiceRepository;
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
public class InvoicesService {
    private final InvoicesRepository invoicesRepository;
    private final SuppliersRepository suppliersRepository;
    private final MetersRepository metersRepository;
    private TypeOfMeterAndInvoiceRepository typeOfMeterAndInvoiceRepository;

    @Autowired
    public InvoicesService(InvoicesRepository invoicesRepository, SuppliersRepository suppliersRepository, MetersRepository metersRepository,
                           TypeOfMeterAndInvoiceRepository typeOfMeterAndInvoiceRepository) {
        this.invoicesRepository = invoicesRepository;
        this.suppliersRepository = suppliersRepository;
        this.metersRepository = metersRepository;
        this.typeOfMeterAndInvoiceRepository=typeOfMeterAndInvoiceRepository;
    }

    public Invoices addInvoice(Invoices invoice) {
        return invoicesRepository.save(invoice);
    }

    public List<Invoices> findAll() {
        return invoicesRepository.findAll();
    }

    public List<Suppliers> findAllSuppliers() {
        return suppliersRepository.findAll();
    }

    public List<Meters> findAllMeters() {
        return metersRepository.findAll();
    }
    public List<TypeOfMeterAndInvoice> findAllTyepOfMeterAndInvoice() {
        return typeOfMeterAndInvoiceRepository.findAll();
    }

    public Suppliers getSupplierByInvoiceId(Integer id) {
        Optional<Invoices> invoice = invoicesRepository.findById(id);
        return invoice.get().getSupplier();
    }

    public Meters getMetersByInvoiceId(Integer id) {
        Optional<Invoices> invoice = invoicesRepository.findById(id);
        return invoice.get().getMeter();
    }
    public TypeOfMeterAndInvoice getTypeOfMeterAndInvoiceByInvoiceId(Integer id) {
        Optional<Invoices> invoice = invoicesRepository.findById(id);
        return invoice.get().getTypeOfMeterAndInvoice();
    }


    public Invoices updateInvoice(Invoices invoice) {
        return invoicesRepository.save(invoice);
    }


    public Invoices findInvoiceById(Integer id) throws InvoicesNotFoundException {
        return invoicesRepository.findById(id)
                .orElseThrow(() -> new InvoicesNotFoundException(id));
    }

    public void deleteInvoice(Integer id) throws InvoicesNotFoundException {
        final Invoices invoice = this.invoicesRepository.findById(id).orElseThrow(() -> new InvoicesNotFoundException(id));
        invoicesRepository.delete(invoice);
    }

    public Page<Invoices> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.invoicesRepository.findAll(pageable);
    }

}
