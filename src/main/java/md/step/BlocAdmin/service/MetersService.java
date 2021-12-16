package md.step.BlocAdmin.service;

import md.step.BlocAdmin.exception.MetersNotFoundException;
import md.step.BlocAdmin.model.*;
import md.step.BlocAdmin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MetersService {
    private MetersRepository metersRepository;
    private MeterDestRepository meterDestRepository;
    private SuppliersRepository suppliersRepository;
    private PersonRepository personRepository;
    private FlatsRepository flatsRepository;
    private BuildingsRepository buildingsRepository;
    private InvoicesRepository invoicesRepository;
    private TypeOfMeterInvoiceRepository typeOfMeterInvoiceRepository;

    @Autowired
    public MetersService(MetersRepository metersRepository, MeterDestRepository meterDestRepository, SuppliersRepository suppliersRepository, FlatsRepository flatsRepository,
                         TypeOfMeterInvoiceRepository typeOfMeterInvoiceRepository, PersonRepository personRepository,
                         InvoicesRepository invoicesRepository, BuildingsRepository buildingsRepository) {
        this.metersRepository = metersRepository;
        this.meterDestRepository = meterDestRepository;
        this.suppliersRepository = suppliersRepository;
        this.personRepository = personRepository;
        this.flatsRepository = flatsRepository;
        this.invoicesRepository = invoicesRepository;
        this.typeOfMeterInvoiceRepository = typeOfMeterInvoiceRepository;
        this.buildingsRepository = buildingsRepository;
    }

    public Meters addMeter(Meters meter) {
        return metersRepository.save(meter);
    }

    public List<Meters> findAll() {
        return metersRepository.findAll();
    }

    public List<MeterDest> findAllMeterType() {
        return meterDestRepository.findAll();
    }

    public List<TypeOfMeterInvoice> findAllTypeOfMeterInvoice() {
        return typeOfMeterInvoiceRepository.findAll();
    }

    public List<Suppliers> findAllSuppliers() {
        return suppliersRepository.findAll();
    }

    public List<Person> findAllPerson() {
        return personRepository.findAll();
    }

    public List<Flats> findAllFlats() {
        return flatsRepository.findAll();
    }

    public List<Buildings> findAllBuildings() {
        return buildingsRepository.findAll();
    }

    public Boolean checkSerialExist(String serial) {
        return metersRepository.existsBySerial(serial);
    }


    public Suppliers getSupplierByMeterId(Integer id) {
        Optional<Meters> meter = metersRepository.findById(id);
        return meter.get().getSupplier();
    }

    public Person getPersonsByMeterId(Integer id) {
        Optional<Meters> meter = metersRepository.findById(id);
        return meter.get().getPerson();
    }

    public Buildings getBuildingByMeterId(Integer id) {
        Optional<Meters> meter = metersRepository.findById(id);
        return meter.get().getBuilding();
    }

    public MeterDest getMeterDestByMeterId(Integer id) {
        Optional<Meters> meter = metersRepository.findById(id);
        return meter.get().getMeterDest();
    }

    public TypeOfMeterInvoice getTypeOfMeterInvoiceByMeterId(Integer id) {
        Optional<Meters> meter = metersRepository.findById(id);
        return meter.get().getTypeOfMeterInvoice();
    }

    public Flats getFlatByMetersId(Integer id) {
        Optional<Meters> meters = metersRepository.findById(id);
        return meters.get().getFlat();
    }

    public List<Flats> getFlatByBuilding(Buildings building) {
        List<Flats> flats = flatsRepository.findFlatsByBuilding(building);
        return flats;
    }


    public Meters updateMeter(Meters meter) {
        return metersRepository.save(meter);
    }


    public Meters findMeterById(Integer id) throws MetersNotFoundException {
        return metersRepository.findById(id)
                .orElseThrow(() -> new MetersNotFoundException(id));
    }

    public void deleteMeter(Integer id) throws MetersNotFoundException {
        final Meters meter = this.metersRepository.findById(id).orElseThrow(() -> new MetersNotFoundException(id));
        metersRepository.delete(meter);
    }

    public Page<Meters> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.metersRepository.findAll(pageable);
    }

}

