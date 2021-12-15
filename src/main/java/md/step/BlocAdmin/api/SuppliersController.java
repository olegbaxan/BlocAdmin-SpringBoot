package md.step.BlocAdmin.api;


import md.step.BlocAdmin.exception.SuppliersNotFoundException;
import md.step.BlocAdmin.model.Address;
import md.step.BlocAdmin.model.Suppliers;
import md.step.BlocAdmin.repository.AddressRepository;
import md.step.BlocAdmin.repository.SuppliersRepository;
import md.step.BlocAdmin.service.AddressService;
import md.step.BlocAdmin.service.SuppliersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(maxAge = 3600, allowCredentials = "true",origins = "*")
@RestController
@RequestMapping("/api/v1/suppliers")
public class SuppliersController {
    private final SuppliersService suppliersService;
    private final AddressService addressService;

    @Autowired
    private SuppliersRepository suppliersRepository;
    @Autowired
    private AddressRepository addressRepository;


    @Autowired
    public SuppliersController(SuppliersService suppliersService,AddressService addressService) {
        this.suppliersService = suppliersService;
        this.addressService=addressService;
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllSuppliers(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Suppliers> suppliers = new ArrayList<Suppliers>();
            Pageable paging = PageRequest.of(page, size);

            Page<Suppliers> pageSuppliers;
            if (title == null) {
                pageSuppliers = suppliersRepository.findAll(paging);
            } else {
                pageSuppliers = suppliersRepository.findDistinctBySupplierNameContainingIgnoreCaseOrAddress_CityStartingWithIgnoreCaseOrAddress_RaionStartingWithIgnoreCaseOrAddress_StreetStartingWithIgnoreCase(title, title, title, title,paging);

            }

            suppliers = pageSuppliers.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("suppliers", suppliers);
            response.put("currentPage", pageSuppliers.getNumber());
            response.put("totalItems", pageSuppliers.getTotalElements());
            response.put("totalPages", pageSuppliers.getTotalPages());

            System.out.println("response = " + response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("address")
    public ResponseEntity<List<Address>> getAddress() {
        List<Address> address = suppliersService.findAllAddresses();
        return ResponseEntity.ok(address);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @GetMapping("/{id}")
    public ResponseEntity<Suppliers> getSupplierById(@PathVariable("id") Integer id) throws SuppliersNotFoundException {
        Suppliers supplier = suppliersService.findSupplierById(id);
        return new ResponseEntity<>(supplier, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @PostMapping()
    public ResponseEntity<Suppliers> addSuppliers(@RequestBody Suppliers supplier) {
        Address address = new Address();
        System.out.println(supplier.getAddress());
        address=addressRepository.findAllByAddressid(supplier.getAddress().getAddressid());

        supplier.setAddress(address);
        suppliersRepository.save(supplier);

        return new ResponseEntity<>(supplier, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @PutMapping()
    public ResponseEntity<Suppliers> updateSupplier(@RequestBody Suppliers supplier) {
        Address address = new Address();
        address=addressRepository.findAllByAddressid(supplier.getAddress().getAddressid());

        supplier.setAddress(address);
        Suppliers updateSupplier = suppliersService.updateSupplier(supplier);
        return new ResponseEntity<>(updateSupplier, HttpStatus.OK);
    }
    @PreAuthorize(("hasRole('ROLE_ADMIN')")+(" || hasRole('ROLE_BLOCADMIN')"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable("id") Integer id) throws SuppliersNotFoundException {
        suppliersService.deleteSupplier(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
