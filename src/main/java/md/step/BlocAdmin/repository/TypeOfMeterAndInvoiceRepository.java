package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.TypeOfMeterAndInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeOfMeterAndInvoiceRepository extends JpaRepository<TypeOfMeterAndInvoice, Integer> {
    TypeOfMeterAndInvoice findAllById (Integer id);
}
