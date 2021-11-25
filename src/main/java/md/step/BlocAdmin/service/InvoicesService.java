package md.step.BlocAdmin.service;

import md.step.BlocAdmin.exception.InvoicesNotFoundException;
import md.step.BlocAdmin.model.*;
import md.step.BlocAdmin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class InvoicesService {
    private final InvoicesRepository invoicesRepository;
    private final SuppliersRepository suppliersRepository;
    private final MetersRepository metersRepository;
    private final FileDBRepository fileDBRepository;
    private final BuildingsRepository buildingsRepository;
    private TypeOfMeterInvoiceRepository typeOfMeterInvoiceRepository;

    @Autowired
    public InvoicesService(InvoicesRepository invoicesRepository, SuppliersRepository suppliersRepository, MetersRepository metersRepository,
                           TypeOfMeterInvoiceRepository typeOfMeterInvoiceRepository, FileDBRepository fileDBRepository,
                           BuildingsRepository buildingsRepository) {
        this.invoicesRepository = invoicesRepository;
        this.suppliersRepository = suppliersRepository;
        this.metersRepository = metersRepository;
        this.typeOfMeterInvoiceRepository = typeOfMeterInvoiceRepository;
        this.fileDBRepository=fileDBRepository;
        this.buildingsRepository=buildingsRepository;
    }

    public Invoices addInvoice(Invoices invoice) {
        return invoicesRepository.save(invoice);
    }

    public List<Invoices> findAll() {
        return invoicesRepository.findAll();
    }
    public List<Invoices> getInvoicesByFlatId(Integer flatId,Status status) {
        return invoicesRepository.findInvoicesByMeter_Flat_FlatidAndStatus(flatId,status);
    }

    public List<Suppliers> findAllSuppliers() {
        return suppliersRepository.findAll();
    }

    public List<Meters> findAllMeters() {
        return metersRepository.findAll();
    }
    public List<Buildings> findAllBuildings() {
        return buildingsRepository.findAll();
    }
    public List<TypeOfMeterInvoice> findAllTyepOfMeterAndInvoice() {
        return typeOfMeterInvoiceRepository.findAll();
    }

    public Suppliers getSupplierByInvoiceId(Integer id) {
        Optional<Invoices> invoice = invoicesRepository.findById(id);
        return invoice.get().getSupplier();
    }
    public Set<Buildings> getBuildingsByInvoiceId(Integer id) {
        Optional<Invoices> invoice = invoicesRepository.findById(id);
        return invoice.get().getBuildings();
    }

    public Meters getMetersByInvoiceId(Integer id) {
        Optional<Invoices> invoice = invoicesRepository.findById(id);
        return invoice.get().getMeter();
    }
//    public FileDB getInvoiceFileByInvoiceId(Integer id) {
//        Optional<Invoices> invoice = invoicesRepository.findById(id);
//        return invoice.get().getInvoiceFileId();
//    }
    public FileDB getFileByName(String id) {
    return fileDBRepository.findAllByName(id);
}

    public TypeOfMeterInvoice getTypeOfMeterAndInvoiceByInvoiceId(Integer id) {
        Optional<Invoices> invoice = invoicesRepository.findById(id);
        return invoice.get().getTypeOfMeterInvoice();
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
