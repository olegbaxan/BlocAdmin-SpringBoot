package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Payments;
import md.step.BlocAdmin.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentsRepository extends JpaRepository<Payments, Integer> {
    List<Payments> findAllByPerson(Person person);
}
