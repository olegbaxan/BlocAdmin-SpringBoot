package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Payments;
import md.step.BlocAdmin.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentsRepository extends JpaRepository<Payments, Integer> {
    List<Payments> findAllByPerson(Person person);
    Page<Payments> findDistinctByPerson_NameContainingIgnoreCaseOrPerson_SurnameContainingIgnoreCase(String name, String surname, Pageable pageable);
}
