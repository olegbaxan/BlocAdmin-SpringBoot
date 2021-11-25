package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileDBRepository extends JpaRepository<FileDB, String> {
    FileDB findAllByFileid(String invoice);

    @Query(value = "SELECT *\n" +"\tFROM application.files\n" + "\twhere\n" +"\tname = '?1'",nativeQuery = true)
    FileDB findByFileid(String fileName);

    FileDB findAllByName(String fileName);


}
