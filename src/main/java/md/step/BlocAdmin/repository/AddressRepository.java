package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {

//    List<Address> findByCityStartingWithOrRaionStartingWithOrStreetStartingWithOrHouseNumberStartingWith(String city, String raion, String street, String houseNumber, Pageable pageable);
    Page<Address> findByCityStartingWithIgnoreCaseOrRaionStartingWithIgnoreCaseOrStreetStartingWithIgnoreCaseOrHouseNumberStartingWithIgnoreCase(String city, String raion, String street, String houseNumber, Pageable pageable);

    Address findAllByAddressid(Integer id);
//    Page<Address> findAll(Pageable pageable);
//    Page<Address> findByCity(String city, Pageable pageable);
//    Page<Address> findByTitleContaining(String title, Pageable pageable);
}
