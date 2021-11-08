package md.step.BlocAdmin.api;

import md.step.BlocAdmin.exception.AddressNotFoundException;
import md.step.BlocAdmin.model.Address;
import md.step.BlocAdmin.repository.AddressRepository;
import md.step.BlocAdmin.service.AddressService;
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


@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    private final AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

//    @GetMapping()
//    public ResponseEntity<List<Address>> getAllAddresses () {
//        List<Address> address = addressService.findAllAddresses();
//        return new ResponseEntity<>(address, HttpStatus.OK);
//    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllAddresses(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Address> addresses = new ArrayList<Address>();
            Pageable paging = PageRequest.of(page, size);
//            Pageable paging = Pageable.unpaged();
            System.out.println("paging = "+paging);

            Page<Address> pageAddresses;
            if (title == null) {
                pageAddresses = addressRepository.findAll(paging);
                System.out.println("pageAddress = "+pageAddresses);
            } else {
                pageAddresses = addressRepository.findByCityStartingWithOrRaionStartingWithOrStreetStartingWithOrHouseNumberStartingWith(title, title, title, title, paging);
                System.out.println("pageAddress2 = " + pageAddresses);
            }

            addresses = pageAddresses.getContent();
            System.out.println("address = "+addresses);
            Map<String, Object> response = new HashMap<>();
            response.put("addresses", addresses);
            response.put("currentPage", pageAddresses.getNumber());
            response.put("totalItems", pageAddresses.getTotalElements());
            response.put("totalPages", pageAddresses.getTotalPages());

            System.out.println("response = "+response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById (@PathVariable("id") Integer id) throws AddressNotFoundException {
        Address address = addressService.findAddressById(id);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Address> addAddress(@RequestBody Address address) {
        Address newAddress = addressService.addAddress(address);
        return new ResponseEntity<>(newAddress, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<Address> updateAddress(@RequestBody Address address) {
        Address updateAddress = addressService.updateAddress(address);
        return new ResponseEntity<>(updateAddress, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable("id") Integer id) throws AddressNotFoundException {
        addressService.deleteAddress(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
