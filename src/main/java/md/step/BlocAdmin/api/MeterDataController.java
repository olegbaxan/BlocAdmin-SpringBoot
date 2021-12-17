package md.step.BlocAdmin.api;

import md.step.BlocAdmin.exception.MeterDataNotFoundException;
import md.step.BlocAdmin.model.*;
import md.step.BlocAdmin.repository.*;
import md.step.BlocAdmin.service.MeterDataService;
import md.step.BlocAdmin.service.MetersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(maxAge = 3600, allowCredentials = "true", origins = "https://blocadmin-angularui.herokuapp.com/")
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
    @Autowired
    private SuppliersRepository suppliersRepository;
    @Autowired
    private TypeOfMeterInvoiceRepository typeOfMeterInvoiceRepository;

    public MeterDataController(MeterDataService meterDataService, MetersService metersService, StatusRepository statusRepository,
                               TypeOfMeterInvoiceRepository typeOfMeterInvoiceRepository, SuppliersRepository suppliersRepository) {
        this.meterDataService = meterDataService;
        this.metersService = metersService;
        this.statusRepository = statusRepository;
        this.typeOfMeterInvoiceRepository = typeOfMeterInvoiceRepository;
        this.suppliersRepository = suppliersRepository;
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllMeterData(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<MeterData> meterData = new ArrayList<MeterData>();
            Pageable paging = PageRequest.of(page, size, Sort.by("meterdataid").descending());

            Page<MeterData> pageMeterData;
            if (title == null) {
                pageMeterData = meterDataRepository.findAll(paging);
            } else {
                pageMeterData = meterDataRepository.findDistinctByMeter_SerialContainingIgnoreCaseOrMeter_Supplier_SupplierNameContainingIgnoreCaseOrMeter_Person_NameContainingIgnoreCaseOrMeter_Person_SurnameContainingIgnoreCaseOrMeter_Building_Address_CityStartingWithIgnoreCaseOrMeter_Building_Address_RaionStartingWithIgnoreCaseOrMeter_Building_Address_StreetStartingWithIgnoreCase(title, title, title, title, title, title, title, paging);
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

    @GetMapping("new")
    public ResponseEntity<Map<String, Object>> getMeterDataNew(
            @RequestParam(required = false, defaultValue = "%") String supp,
            @RequestParam(defaultValue = "0") int build
//            @RequestParam(defaultValue = "3") int size
    ) {

        try {
            List<MeterData> meterData = new ArrayList<MeterData>();
//            Pageable paging = PageRequest.of(page, size);

//            MeterData pageMeterData;
            if (Objects.equals(supp, "%") && build == 0) {
                TypeOfMeterInvoice type = typeOfMeterInvoiceRepository.findAllById(Integer.parseInt("3"));//TYPE_FLATS
                meterData = meterDataRepository.findMeterDataByStatus_NameAndMeter_TypeOfMeterInvoice(EStatus.STATUS_NEW, type);

            } else if (!Objects.equals(supp, "%") && build == 0) {
                TypeOfMeterInvoice type = typeOfMeterInvoiceRepository.findAllById(Integer.parseInt("3"));
                meterData = meterDataRepository.findMeterDataByStatus_NameAndMeter_TypeOfMeterInvoiceAndMeter_Supplier_SupplierName(EStatus.STATUS_NEW, type, supp);
            } else if (Objects.equals(supp, "%") && build > 0) {
                TypeOfMeterInvoice type = typeOfMeterInvoiceRepository.findAllById(Integer.parseInt("3"));
                meterData = meterDataRepository.findMeterDataByStatus_NameAndMeter_TypeOfMeterInvoiceAndMeter_Flat_Building_Buildingid(EStatus.STATUS_NEW, type, build);
            } else {
                TypeOfMeterInvoice type = typeOfMeterInvoiceRepository.findAllById(Integer.parseInt("3"));
                meterData = meterDataRepository.findMeterDataByStatus_NameAndMeter_TypeOfMeterInvoiceAndMeter_Supplier_SupplierNameAndMeter_Flat_Building_Buildingid(EStatus.STATUS_NEW, type, supp, build);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("meterData", meterData);
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

    @GetMapping("maxprev/{id}")
    public ResponseEntity<MeterData> getMaxPrevMeterDataByMeterId(@PathVariable("id") Integer meterId) {
        MeterData meterData = meterDataService.getMaxPrevMeterDataByMetersId(meterId);
        return ResponseEntity.ok(meterData);
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("invoices/{name}")
    public ResponseEntity<List<Invoices>> getInvoicesBySupplier(@PathVariable("name") String supplierName) {
        List<Invoices> invoices = meterDataService.findAllInvoicesBySupplier(supplierName);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("flats/{id}")
    public ResponseEntity<List<Flats>> getFlatsByBuilding(@PathVariable("id") Integer buildingId) {
        List<Flats> flats = meterDataService.findAllFlatsByBuilding(buildingId);
        return ResponseEntity.ok(flats);
    }

    @GetMapping("status")
    public ResponseEntity<List<Status>> getStatus() {
        List<Status> status = meterDataService.findAllStatus();
        return ResponseEntity.ok(status);
    }

    @GetMapping("/getprevious/{id}")
    public ResponseEntity<Double> findMaxCurrentValueByMeterId(@PathVariable("id") Integer id) throws MeterDataNotFoundException {
        Double maxValue = meterDataRepository.findMaxCurrentValueByMeterId(id);
        System.out.println("MeterData = "+maxValue);
        return new ResponseEntity<>(maxValue, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeterData> getMeterDataById(@PathVariable("id") Integer id) throws MeterDataNotFoundException {
        MeterData meterData = meterDataService.findMeterDataById(id);
        return new ResponseEntity<>(meterData, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<MeterData> addMeterData(@RequestBody MeterData meterData) {
        Meters meter = new Meters();
        meter = metersRepository.findAllByMeterid(meterData.getMeter().getMeterId());
        meterData.setMeter(meter);


        Status status = statusRepository.findAllById(meterData.getStatus().getId());
        meterData.setStatus(status);
        meterData.setMeterValue(meterData.getCurrentValue() - meterData.getPreviousValue());

        meterDataRepository.save(meterData);

        return new ResponseEntity<>(meterData, HttpStatus.OK);
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
    @PostMapping("addbulk")
    public ResponseEntity<List<MeterData>> addBulkMeterData(@RequestBody List<MeterData> meterData) {
        for (int i = 0; i < meterData.size(); i++) {
            Meters meter = new Meters();
            meter = metersRepository.findAllByMeterid(meterData.get(i).getMeter().getMeterId());
            meterData.get(i).setMeter(meter);

            if (meterData.get(i).getStatus() != null) {
                Status status = statusRepository.findAllById(meterData.get(i).getStatus().getId());
                meterData.get(i).setStatus(status);
            } else {
                Status status = statusRepository.findByName(EStatus.STATUS_CLOSED);
                meterData.get(i).setStatus(status);
            }

            meterData.get(i).setMeterValue(meterData.get(i).getCurrentValue() - meterData.get(i).getPreviousValue());
            meterDataService.addMeterData(meterData.get(i));
        }
        return new ResponseEntity<>(meterData, HttpStatus.OK);
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
    @PutMapping()
    public ResponseEntity<MeterData> updateMeterData(@RequestBody MeterData meterData) {
        Meters meter = new Meters();
        meter = metersRepository.findAllByMeterid(meterData.getMeter().getMeterId());
        meterData.setMeter(meter);
        meterData.setMeterValue(meterData.getCurrentValue() - meterData.getPreviousValue());
        MeterData updateMeterData = meterDataService.updateMeterData(meterData);
        return new ResponseEntity<>(updateMeterData, HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<MeterData> updateCurrentValue(@RequestBody Integer id, String current) throws MeterDataNotFoundException {
        MeterData meterData = meterDataService.findMeterDataById(id);
        meterData.setCurrentValue(Double.parseDouble(current));
        MeterData updateMeterData = meterDataService.updateMeterData(meterData);
        return new ResponseEntity<>(updateMeterData, HttpStatus.OK);
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN')") + (" || hasRole('ROLE_BLOCADMIN')"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeterData(@PathVariable("id") Integer id) throws MeterDataNotFoundException {
        meterDataService.deleteMeterData(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
