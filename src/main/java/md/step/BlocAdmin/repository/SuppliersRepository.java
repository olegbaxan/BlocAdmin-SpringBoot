package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Suppliers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuppliersRepository extends JpaRepository<Suppliers, Integer> {
    Suppliers findAllBySupplierid(Integer supplierId);
}
