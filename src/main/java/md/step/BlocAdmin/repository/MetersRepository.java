package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Meters;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetersRepository extends JpaRepository<Meters, Integer> {
    Meters findAllByMeterid(Integer id);
    List<Meters> findAllBySupplier_SupplierNameAndFlat_Building_Buildingid(String supplierName,Integer buildingId);
    List<Meters> findAllBySupplier_SupplierNameAndFlat_Building_BuildingidAndAndFlat_Entrance(String supplierName,Integer ladder,Integer buildingId);
}
