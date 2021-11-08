package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.EMeterType;
import md.step.BlocAdmin.model.MeterType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeterTypeRepository extends JpaRepository<MeterType, Integer> {
    Optional<MeterType> findByName(EMeterType name);

    MeterType findAllByMetertypeid(Integer id);
}
