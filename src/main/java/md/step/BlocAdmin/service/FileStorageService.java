package md.step.BlocAdmin.service;

import md.step.BlocAdmin.model.FileDB;
import md.step.BlocAdmin.repository.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileStorageService {
    @Autowired
    private FileDBRepository fileDBRepository;

    public FileDB store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB FileDB = new FileDB(fileName, file.getContentType(), file.getBytes());

        return fileDBRepository.save(FileDB);
    }

    public FileDB getFile(String id) {
        System.out.println("FileId = "+id);
        return fileDBRepository.findById(id).get();
    }


    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }
}
