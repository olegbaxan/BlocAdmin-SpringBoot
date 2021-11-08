package md.step.BlocAdmin.service;

import md.step.BlocAdmin.exception.BuildingsNotFoundException;
import md.step.BlocAdmin.model.Address;
import md.step.BlocAdmin.model.Buildings;
import md.step.BlocAdmin.repository.AddressRepository;
import md.step.BlocAdmin.repository.BuildingsRepository;
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
public class BuidingsService {
    private final BuildingsRepository buildingsRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public BuidingsService(BuildingsRepository buildingsRepository, AddressRepository addressRepository) {

        this.buildingsRepository = buildingsRepository;
        this.addressRepository = addressRepository;
    }

    public Buildings addBuilding(Buildings building) {
        return buildingsRepository.save(building);
    }

    public List<Buildings> findAll() {
        return buildingsRepository.findAll();
    }

    public List<Address> findAllAddresses() {
        return addressRepository.findAll();
    }

    public Address getAddressByBuildingsId(Integer id) {
        Optional<Buildings> building= buildingsRepository.findById(id);
        return building.get().getAddress();
    }

    public Buildings updateBuilding(Buildings building) {
        return buildingsRepository.save(building);
    }


    public Buildings findBuildingById(Integer id) throws BuildingsNotFoundException {
        return buildingsRepository.findById(id)
                .orElseThrow(() -> new BuildingsNotFoundException(id));
    }

    public void deleteBuilding(Integer id) throws BuildingsNotFoundException {
        final Buildings building = this.buildingsRepository.findById(id).orElseThrow(() -> new BuildingsNotFoundException(id));
        buildingsRepository.delete(building);
    }

    public Page<Buildings> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.buildingsRepository.findAll(pageable);
    }
}
