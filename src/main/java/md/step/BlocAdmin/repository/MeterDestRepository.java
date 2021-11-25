package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.EMeterDest;
import md.step.BlocAdmin.model.MeterDest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeterDestRepository extends JpaRepository<MeterDest, Integer> {
    Optional<MeterDest> findByName(EMeterDest name);

    MeterDest findAllByMetertypeid(Integer id);
}
