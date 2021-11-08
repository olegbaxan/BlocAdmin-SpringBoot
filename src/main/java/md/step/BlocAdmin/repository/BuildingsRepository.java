package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Buildings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingsRepository extends JpaRepository<Buildings, Integer> {
    Buildings findAllByBuildingid(Integer buildingid);
}
