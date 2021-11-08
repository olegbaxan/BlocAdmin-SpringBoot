package md.step.BlocAdmin.service;

import md.step.BlocAdmin.exception.SuppliersNotFoundException;
import md.step.BlocAdmin.model.Address;
import md.step.BlocAdmin.model.Suppliers;
import md.step.BlocAdmin.repository.AddressRepository;
import md.step.BlocAdmin.repository.SuppliersRepository;
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
public class SuppliersService {
    private final SuppliersRepository suppliersRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public SuppliersService(SuppliersRepository suppliersRepository, AddressRepository addressRepository) {

        this.suppliersRepository = suppliersRepository;
        this.addressRepository = addressRepository;
    }


    public Suppliers addSupplier(Suppliers supplier) {
        return suppliersRepository.save(supplier);
    }

    public List<Suppliers> findAll() {
        return suppliersRepository.findAll();
    }

    public List<Address> findAllAddresses() {
        return addressRepository.findAll();
    }

    public Address getAddressBySuppliersId(Integer id) {
        Optional<Suppliers> supplier= suppliersRepository.findById(id);
        Address address =supplier.get().getAddress();
        return address;
    }

    public Suppliers updateSupplier(Suppliers supplier) {
        return suppliersRepository.save(supplier);
    }


    public Suppliers findSupplierById(Integer id) throws SuppliersNotFoundException {
        return suppliersRepository.findById(id)
                .orElseThrow(() -> new SuppliersNotFoundException(id));
    }

    public void deleteSupplier(Integer id) throws SuppliersNotFoundException {
        final Suppliers supplier = this.suppliersRepository.findById(id).orElseThrow(() -> new SuppliersNotFoundException(id));
        suppliersRepository.delete(supplier);
    }

    public Page<Suppliers> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.suppliersRepository.findAll(pageable);
    }

}
