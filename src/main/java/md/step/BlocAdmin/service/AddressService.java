package md.step.BlocAdmin.service;

import md.step.BlocAdmin.exception.AddressNotFoundException;
import md.step.BlocAdmin.model.Address;
import md.step.BlocAdmin.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AddressService {
    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }


    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }

    public List<Address> findAllAddresses() {
        return addressRepository.findAll();
    }

    public Address updateAddress(Address address) {
        return addressRepository.save(address);
    }

    public Address findAddressById(Integer id) throws AddressNotFoundException {
        return addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException( id));
    }

    public void deleteAddress(Integer id) throws AddressNotFoundException {
        final Address address = this.addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id));
        addressRepository.delete(address);
    }

//    public Page<Address> getFilteredData(Address address) {
//        return addressRepository.findByCityStartingWithOrRaionStartingWithOrStreetStartingWithOrHouseNumberStartingWith(address.getCity(), address.getRaion(), address.getStreet(), address.getHouseNumber());
//    }
//    @Override
    public Page<Address> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.addressRepository.findAll(pageable);
    }
}
