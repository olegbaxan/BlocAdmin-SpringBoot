package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Buildings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingsRepository extends JpaRepository<Buildings, Integer> {
    Buildings findAllByBuildingid(Integer buildingid);
    Page<Buildings> findAllByAddress_CityStartingWithIgnoreCaseOrAddress_RaionStartingWithIgnoreCaseOrAddress_StreetStartingWithIgnoreCase(String city, String raion,String street, Pageable pageable);
}
