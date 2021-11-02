package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.ERole;
import md.step.BlocAdmin.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
//    List<Role> findAll();
}
