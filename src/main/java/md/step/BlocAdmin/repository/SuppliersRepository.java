package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Suppliers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuppliersRepository extends JpaRepository<Suppliers, Integer> {
    Suppliers findAllBySupplierid(Integer supplierId);
    Suppliers findAllBySupplierName(String supplier);

    Page<Suppliers> findDistinctBySupplierNameContainingIgnoreCaseOrAddress_CityStartingWithIgnoreCaseOrAddress_RaionStartingWithIgnoreCaseOrAddress_StreetStartingWithIgnoreCase(String name, String city, String raion, String street, Pageable pageable);
}
