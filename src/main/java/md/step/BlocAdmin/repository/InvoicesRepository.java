package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoicesRepository extends JpaRepository<Invoices, Integer> {
    Invoices findInvoiceByInvoiceId(Integer id);
    List<Invoices> findAllBySupplier_SupplierNameAndStatusNameAndTypeOfMeterInvoiceName(String supplierName, EStatus status, ETypeOfMeterInvoice type);
    List<Invoices> findInvoicesByMeter_PersonOrMeter_Flat_Person(Person contactPerson, Person flatPerson);
    List<Invoices> findFirst10InvoicesByMeter_PersonOrMeter_Flat_PersonOrderByEmittedDateDesc(Person contactPerson, Person flatPerson);
    List<Invoices> findFirst10InvoicesByTypeOfMeterInvoiceOrderByEmittedDateDesc(TypeOfMeterInvoice type);
    List<Invoices> findInvoicesByMeter_Flat_FlatidAndStatus(Integer flatId, Status status);
}
