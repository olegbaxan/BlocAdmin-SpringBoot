package md.step.BlocAdmin.api;

import md.step.BlocAdmin.exception.MeterDataNotFoundException;
import md.step.BlocAdmin.model.MeterData;
import md.step.BlocAdmin.model.Meters;
import md.step.BlocAdmin.model.Status;
import md.step.BlocAdmin.repository.MeterDataRepository;
import md.step.BlocAdmin.repository.MetersRepository;
import md.step.BlocAdmin.repository.StatusRepository;
import md.step.BlocAdmin.service.MeterDataService;
import md.step.BlocAdmin.service.MetersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/meterdata")
public class MeterDataController {
    public final MeterDataService meterDataService;
    public final StatusRepository statusRepository;
    public final MetersService metersService;

    @Autowired
    private MeterDataRepository meterDataRepository;
    @Autowired
    private MetersRepository metersRepository;

    public MeterDataController(MeterDataService meterDataService, MetersService metersService,StatusRepository statusRepository) {
        this.meterDataService = meterDataService;
        this.metersService = metersService;
        this.statusRepository=statusRepository;
    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllMeterData(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<MeterData> meterData = new ArrayList<MeterData>();
            Pageable paging = PageRequest.of(page, size);

            Page<MeterData> pageMeterData;
            if (title == null) {
                pageMeterData = meterDataRepository.findAll(paging);
            } else {
                pageMeterData = meterDataRepository.findAll(paging);
//                pagePersons = personRepository.findByNameStartingWithOrSurnameStartingWithOrIdnpStartingWithOrEmailStartingWithOrPhoneStartingWithOrMobileStartingWith(title, title, title, title,title,title, paging);

            }

            meterData = pageMeterData.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("meterData", meterData);
            response.put("currentPage", pageMeterData.getNumber());
            response.put("totalItems", pageMeterData.getTotalElements());
            response.put("totalPages", pageMeterData.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("meters")
    public ResponseEntity<List<Meters>> getMeters() {
        List<Meters> meters = meterDataService.findAllMeters();
        return ResponseEntity.ok(meters);
    }
    @GetMapping("status")
    public ResponseEntity<List<Status>> getStatus() {
        List<Status> status = meterDataService.findAllStatus();
        return ResponseEntity.ok(status);
    }
    @GetMapping("/getprevious/{id}")
    public ResponseEntity<Double> findMaxCurrentValueByMeterId(@PathVariable("id") Integer id) throws MeterDataNotFoundException {
        Double maxValue = meterDataRepository.findMaxCurrentValueByMeterId(id);
        return new ResponseEntity<>(maxValue, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeterData> getMeterDataById(@PathVariable("id") Integer id) throws MeterDataNotFoundException {
        MeterData meterData = meterDataService.findMeterDataById(id);
        return new ResponseEntity<>(meterData, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<MeterData> addMeterData(@RequestBody MeterData meterData) {
        Meters meter=new Meters();
        meter=metersRepository.findAllByMeterid(meterData.getMeter().getMeterId());
        meterData.setMeter(meter);


        Status status = new Status();
        status= statusRepository.findAllById(meterData.getStatus().getId());
        meterData.setStatus(status);

        meterDataRepository.save(meterData);

        return new ResponseEntity<>(meterData, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<MeterData> updateMeterData(@RequestBody MeterData meterData) {
        Meters meter=new Meters();
        meter=metersRepository.findAllByMeterid(meterData.getMeter().getMeterId());
        meterData.setMeter(meter);

        MeterData updateMeterData = meterDataService.updateMeterData(meterData);
        return new ResponseEntity<>(updateMeterData, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeterData(@PathVariable("id") Integer id) throws MeterDataNotFoundException {
        meterDataService.deleteMeterData(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
