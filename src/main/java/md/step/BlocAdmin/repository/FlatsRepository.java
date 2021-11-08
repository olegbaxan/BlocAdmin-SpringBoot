package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Flats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlatsRepository extends JpaRepository<Flats, Integer> {
    Flats findAllByFlatid(Integer flatid);
//    Flats findFlatsById(Integer id);
}
