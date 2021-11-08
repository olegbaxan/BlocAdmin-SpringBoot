package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Invoices;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoicesRepository extends JpaRepository<Invoices, Integer> {
//    Invoices findInvoiceByInvoiceId(Integer id);
}
