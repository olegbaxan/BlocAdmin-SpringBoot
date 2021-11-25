package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.EStatus;
import md.step.BlocAdmin.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
        Status findByName(EStatus name);

        Status findAllById(Integer id);
        Status findAllByName(String name);
}
