package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Buildings;
import md.step.BlocAdmin.model.Flats;
import md.step.BlocAdmin.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlatsRepository extends JpaRepository<Flats, Integer> {
    Flats findAllByFlatid(Integer flatid);

    List<Flats> findFlatsByPerson(Person person);
    List<Flats> findFlatsByBuilding(Buildings buildings);


    List<Flats> findFlatsByBuilding_Buildingid(Integer id);
    Integer countFlatsByBuilding_Buildingid(Integer id);
}
