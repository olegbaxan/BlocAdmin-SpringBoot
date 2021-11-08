package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Meters;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetersRepository extends JpaRepository<Meters, Integer> {
    Meters findAllByMeterid(Integer id);
}
