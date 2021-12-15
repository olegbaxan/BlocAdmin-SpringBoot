package md.step.BlocAdmin.api;

import md.step.BlocAdmin.message.ResponseFile;
import md.step.BlocAdmin.model.FileDB;
import md.step.BlocAdmin.repository.FileDBRepository;
import md.step.BlocAdmin.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@CrossOrigin(maxAge = 3600, allowCredentials = "true",origins = "*")
//@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    @Autowired
    private FileStorageService storageService;
    @Autowired
    private FileDBRepository fileDBRepository;

    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @PostMapping("/upload")
    public ResponseEntity<Map<String,Object>> uploadFile(@RequestParam("file") MultipartFile file) {
//    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            storageService.store(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();

            //suplimentar am inclus ID-ul fisierului salvat mai sus
            FileDB fileDB = fileDBRepository.findAllByName(file.getOriginalFilename());
            System.out.println("fileId="+fileDB.getFileid());
            //??? Cum includ a ID in ResponceEntity si respectiv cum il citesc in FrontEnd???

            List<ResponseFile> files = storageService.getAllFiles()
                    .filter(c -> c.getName().equals(file.getOriginalFilename()))
                    .map(dbFile -> {
                String fileDownloadUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/v1/files/")
                        .path(dbFile.getFileid())
                        .toUriString();

                return new ResponseFile(
                        dbFile.getName(),
                        fileDownloadUri,
                        dbFile.getType(),
                        dbFile.getData().length);
            }).collect(Collectors.toList());


            Map<String, Object> response = new HashMap<>();
            response.put("fileDB", files);
            response.put("message", message);
            return new ResponseEntity<>(response, HttpStatus.OK);

//            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            System.out.println("Exception = "+e);
            Map<String, Object> response = new HashMap<>();
            response.put("fileDB", "Unknown");
            response.put("message", message);
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);

//            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/v1/files/")
                    .path(dbFile.getFileid())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        System.out.println("ID= "+id);
        FileDB fileDB = storageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }
}
