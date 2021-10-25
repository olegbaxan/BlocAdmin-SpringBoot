package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {

//    List<Address> findByCityStartingWithOrRaionStartingWithOrStreetStartingWithOrHouseNumberStartingWith(String city, String raion, String street, String houseNumber);
//    Page<Address> findByCityStartingWithOrRaionStartingWithOrStreetStartingWithOrHouseNumberStartingWith(String city, String raion, String street, String houseNumber);

//    Page<Address> findAll(Pageable pageable);
//    Page<Address> findByCity(String city, Pageable pageable);
//    Page<Address> findByTitleContaining(String title, Pageable pageable);
}
