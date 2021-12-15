package md.step.BlocAdmin.api;

import md.step.BlocAdmin.exception.BuildingsNotFoundException;
import md.step.BlocAdmin.model.Address;
import md.step.BlocAdmin.model.Buildings;
import md.step.BlocAdmin.repository.AddressRepository;
import md.step.BlocAdmin.repository.BuildingsRepository;
import md.step.BlocAdmin.service.AddressService;
import md.step.BlocAdmin.service.BuidingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(maxAge = 3600, allowCredentials = "true",origins = "https://blocadmin-angularui.herokuapp.com/")
@RestController
@RequestMapping("/api/v1/buildings")
public class BuildingsController {
    private final BuidingsService buidingsService;
    private final AddressService addressService;

    @Autowired
    private BuildingsRepository buildingsRepository;
    @Autowired
    private AddressRepository addressRepository;


    @Autowired
    public BuildingsController(BuidingsService buidingsService,AddressService addressService) {
        this.buidingsService = buidingsService;
        this.addressService=addressService;
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllBuildings(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Buildings> buildings = new ArrayList<Buildings>();
            Pageable paging = PageRequest.of(page, size);

            Page<Buildings> pageBuildings;
            if (title == null) {
                pageBuildings = buildingsRepository.findAll(paging);
            } else {
                pageBuildings = buildingsRepository.findAllByAddress_CityStartingWithIgnoreCaseOrAddress_RaionStartingWithIgnoreCaseOrAddress_StreetStartingWithIgnoreCase(title,title,title,  paging);

            }

            buildings = pageBuildings.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("buildings", buildings);
            response.put("currentPage", pageBuildings.getNumber());
            response.put("totalItems", pageBuildings.getTotalElements());
            response.put("totalPages", pageBuildings.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("address")
    public ResponseEntity<List<Address>> getAddress() {
        List<Address> address = buidingsService.findAllAddresses();
        return ResponseEntity.ok(address);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("/{id}")
    public ResponseEntity<Buildings> getBuildingById(@PathVariable("id") Integer id) throws BuildingsNotFoundException {
        Buildings building = buidingsService.findBuildingById(id);
        return new ResponseEntity<>(building, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @PostMapping()
    public ResponseEntity<Buildings> addBuilding(@RequestBody Buildings building) {
        Address address = new Address();
        address=addressRepository.findAllByAddressid(building.getAddress().getAddressid());

        building.setAddress(address);
        buidingsService.addBuilding(building);

        return new ResponseEntity<>(building, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @PutMapping()
    public ResponseEntity<Buildings> updateBuilding(@RequestBody Buildings building) {
        Buildings updateBuilding = buidingsService.updateBuilding(building);
        return new ResponseEntity<>(updateBuilding, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBuilding(@PathVariable("id") Integer id) throws BuildingsNotFoundException {
        buidingsService.deleteBuilding(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
