package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.TypeOfMeterInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeOfMeterInvoiceRepository extends JpaRepository<TypeOfMeterInvoice, Integer> {
    TypeOfMeterInvoice findAllById (Integer id);
    TypeOfMeterInvoice findAllByName (String name);

//    ETypeOfMeterInvoice (String name);
}
