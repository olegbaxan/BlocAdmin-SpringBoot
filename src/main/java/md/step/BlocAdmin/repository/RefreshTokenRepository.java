package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.Person;
import md.step.BlocAdmin.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    @Override
    Optional<RefreshToken> findById(Integer id);
    @Modifying
    int deleteByPerson(Person person);


    Optional<RefreshToken> findByToken(String token);


}
