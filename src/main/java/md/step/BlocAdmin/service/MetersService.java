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
    private MeterTypeRepository meterTypeRepository;
    private SuppliersRepository suppliersRepository;
    private PersonRepository personRepository;
    private FlatsRepository flatsRepository;
    private TypeOfMeterAndInvoiceRepository typeOfMeterAndInvoiceRepository;

    @Autowired
    public MetersService(MetersRepository metersRepository, MeterTypeRepository meterTypeRepository, SuppliersRepository suppliersRepository, FlatsRepository flatsRepository,
                         TypeOfMeterAndInvoiceRepository typeOfMeterAndInvoiceRepository,PersonRepository personRepository) {
        this.metersRepository = metersRepository;
        this.meterTypeRepository = meterTypeRepository;
        this.suppliersRepository = suppliersRepository;
        this.personRepository = personRepository;
        this.flatsRepository = flatsRepository;
        this.typeOfMeterAndInvoiceRepository = typeOfMeterAndInvoiceRepository;
    }

    public Meters addMeter(Meters meter) {
        return metersRepository.save(meter);
    }

    public List<Meters> findAll() {
        return metersRepository.findAll();
    }

    public List<MeterType> findAllMeterType() {
        return meterTypeRepository.findAll();
    }
    public List<TypeOfMeterAndInvoice> findAllTyepOfMeterAndInvoice() {
        return typeOfMeterAndInvoiceRepository.findAll();
    }

    public List<Suppliers> findAllSuppliers() {
        return suppliersRepository.findAll();
    }
    public List<Person> findAllPerson() {return personRepository.findAll();}
    public List<Flats> findAllFlats() {
        return flatsRepository.findAll();
    }


    public Suppliers getSupplierByMeterId(Integer id) {
        Optional<Meters> meter = metersRepository.findById(id);
        return meter.get().getSupplier();
    }
    public Person getPersonsByMeterId(Integer id) {
        Optional<Meters> meter = metersRepository.findById(id);
        return meter.get().getPerson();
    }

    public MeterType getMeterDestByMeterId(Integer id) {
        Optional<Meters> meter= metersRepository.findById(id);
        return meter.get().getMeterType();
    }
    public TypeOfMeterAndInvoice getTypeOfMeterAndInvoiceByMeterId(Integer id) {
        Optional<Meters> meter= metersRepository.findById(id);
        return meter.get().getTypeOfMeterAndInvoice();
    }

    public Flats getFlatByMetersId(Integer id) {
        Optional<Meters> meters = metersRepository.findById(id);
        return meters.get().getFlat();
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
