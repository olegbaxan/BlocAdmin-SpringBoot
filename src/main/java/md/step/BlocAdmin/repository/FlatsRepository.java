package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Buildings;
import md.step.BlocAdmin.model.Flats;
import md.step.BlocAdmin.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface FlatsRepository extends JpaRepository<Flats, Integer> {
    Flats findAllByFlatid(Integer flatid);

    List<Flats> findFlatsByPerson(Person person);
    List<Flats> findFlatsByBuilding(Buildings buildings);

    Page<Flats> findAllDistinctFlatsByBuilding_Address_CityStartingWithIgnoreCaseOrBuilding_Address_RaionStartingWithIgnoreCaseOrBuilding_Address_StreetStartingWithIgnoreCaseOrPerson_NameStartingWithIgnoreCaseOrPerson_SurnameStartingWithIgnoreCase(String city, String raion, String street,String name, String surname, Pageable pageable);
////

    List<Flats> findFlatsByBuilding_Buildingid(Integer id);
    Set<Flats> findAllByBuilding_Buildingid(Integer id);
    List<Flats> findFlatsByWalletLessThan(Double minim);
    Integer countFlatsByBuilding_Buildingid(Integer id);
}
