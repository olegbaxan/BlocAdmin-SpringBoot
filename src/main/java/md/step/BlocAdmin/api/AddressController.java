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
import org.springframework.ui.Model;
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

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllAddresses(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            List<Address> addresses = new ArrayList<Address>();
            Pageable paging = PageRequest.of(page, size);
            System.out.println("paging = "+paging);

            Page<Address> pageAddresses;
            if (search == null) {
                System.out.println();
                pageAddresses = addressRepository.findAll(paging);
                System.out.println("pageAddress = "+pageAddresses);
            } else
                pageAddresses = addressRepository.findAll(paging);
//            public List<Address> findByCityStartingWithOrRaionStartingWithOrStreetStartingWithOrHouseNumberStartingWith(@PathVariable("search") String search)

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
//    @GetMapping("/search/{search}")
//    public List<Address> findByCityStartingWithOrRaionStartingWithOrStreetStartingWithOrHouseNumberStartingWith(@PathVariable("search") String search) {
//        final Address address=new Address();
//        address.setCity(search);
//        address.setRaion(search);
//        address.setStreet(search);
//        address.setHouseNumber(search);
//        return addressService.getFilteredData(address);
//    }
    ///api/v1/address?page=1&size=3
//    @GetMapping("/page/{pageNo}/of/{pageSize}")
//    public String findPaginated(@PathVariable (value = "page") int pageNo,
//                                @PathVariable (value = "size") int pageSize,
//                                @RequestParam("sortField") String sortField,
//                                @RequestParam("sortDir") String sortDir,
//                                Model model) {
////        int pageSize = 5;
//
//        Page<Address> page = addressService.findPaginated(pageNo, pageSize, sortField, sortDir);
//        List<Address> listEmployees = page.getContent();
//
//        model.addAttribute("currentPage", pageNo);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems", page.getTotalElements());
//
//        model.addAttribute("sortField", sortField);
//        model.addAttribute("sortDir", sortDir);
//        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
//
//        model.addAttribute("listEmployees", listEmployees);
//        return "index";
//    }
}
