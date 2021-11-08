package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.EStatus;
import md.step.BlocAdmin.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Integer> {
        Optional<Status> findByName(EStatus name);

        Status findAllById(Integer id);
}
