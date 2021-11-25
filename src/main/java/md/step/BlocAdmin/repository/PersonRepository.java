package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Person;
import md.step.BlocAdmin.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String username);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByIdnp(String idnp);

    Page<Person> findAllByRoles(Optional<Role> role, Pageable paging);


//@Query()
    Page<Person> findAllByRolesAndNameStartingWithOrSurnameStartingWithOrIdnpStartingWithOrEmailStartingWithOrPhoneStartingWithOrMobileStartingWith(Optional<Role> role,String name, String surname, String idnp, String email,String phone,String mobile, Pageable pageable);

    Person findAllByPersonid(Integer id);

    Optional<Person> findPersonByEmail(String email);
    Optional<Person> findPersonByResetPasswordToken(String resetToken);
}
