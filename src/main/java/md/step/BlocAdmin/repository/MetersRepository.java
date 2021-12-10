package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Meters;
import md.step.BlocAdmin.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetersRepository extends JpaRepository<Meters, Integer> {
    Meters findAllByMeterid(Integer id);
    List<Meters> findAllBySupplier_SupplierNameAndFlat_Building_Buildingid(String supplierName,Integer buildingId);
    List<Meters> findAllBySupplier_SupplierNameAndFlat_Building_BuildingidAndAndFlat_Entrance(String supplierName,Integer ladder,Integer buildingId);
Page<Meters> findDistinctBySerialContainingIgnoreCaseOrSupplier_SupplierNameContainingIgnoreCaseOrPerson_NameContainingIgnoreCaseOrPerson_SurnameContainingIgnoreCaseOrBuilding_Address_CityStartingWithIgnoreCaseOrBuilding_Address_RaionStartingWithIgnoreCaseOrBuilding_Address_StreetStartingWithIgnoreCase(String serial,String supplier,String name, String surname,String city,String raion, String street, Pageable pageable);

List<Meters> findMetersByBuilding_Buildingid(Integer id);
    Boolean existsBySerial(String serial);
    List<Meters> findAllByPerson(Person person);
}
