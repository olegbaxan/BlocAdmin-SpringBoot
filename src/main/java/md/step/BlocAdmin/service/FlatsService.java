package md.step.BlocAdmin.service;

import md.step.BlocAdmin.exception.FlatsNotFoundException;
import md.step.BlocAdmin.model.Buildings;
import md.step.BlocAdmin.model.Flats;
import md.step.BlocAdmin.model.Meters;
import md.step.BlocAdmin.model.Person;
import md.step.BlocAdmin.repository.BuildingsRepository;
import md.step.BlocAdmin.repository.FlatsRepository;
import md.step.BlocAdmin.repository.MetersRepository;
import md.step.BlocAdmin.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class FlatsService {
    private final FlatsRepository flatsRepository;
    private final BuildingsRepository buildingsRepository;
    private final PersonRepository personRepository;
    private final MetersRepository metersRepository;

    @Autowired
    public FlatsService(FlatsRepository flatsRepository,
                        BuildingsRepository buildingsRepository,
                        PersonRepository personRepository,
                        MetersRepository metersRepository) {

        this.flatsRepository = flatsRepository;
        this.buildingsRepository = buildingsRepository;
        this.personRepository = personRepository;
        this.metersRepository = metersRepository;
    }

    public Flats addFlat(Flats flat) {
        return flatsRepository.save(flat);
    }

    public List<Flats> findAll() {
        return flatsRepository.findAll();
    }

    public List<Buildings> findAllBuildings() {
        return buildingsRepository.findAll();
    }

    public List<Meters> findAllMeters() {
        return metersRepository.findAll();
    }

    public List<Person> findAllPerson() {
        return personRepository.findAll();
    }

    public Buildings getBuildingByFlatId(Integer id) {
        Optional<Flats> flat = flatsRepository.findById(id);
        return flat.get().getBuilding();
    }

    public Set<Meters> getMetersByFlatId(Integer id) {
        Optional<Flats> flat = flatsRepository.findById(id);
        return flat.get().getMeters();
    }

    public Set<Person> getPersonsByFlatId(Integer id) {
        Optional<Flats> flats = flatsRepository.findById(id);
        Set<Person> personList = flats.get().getPerson();
        return personList;
    }

    public Flats updateFlat(Flats flat) {
        return flatsRepository.save(flat);
    }


    public Flats findFlatById(Integer id) throws FlatsNotFoundException {
        return flatsRepository.findById(id)
                .orElseThrow(() -> new FlatsNotFoundException(id));
    }

    public List<Flats> findFlatsByPersonId(Integer id) throws FlatsNotFoundException {
        Person person = personRepository.findAllByPersonid(id);
        return flatsRepository.findFlatsByPerson(person);
    }

    public void deleteFlat(Integer id) throws FlatsNotFoundException {
        final Flats flat = this.flatsRepository.findById(id).orElseThrow(() -> new FlatsNotFoundException(id));
        flatsRepository.delete(flat);
    }

    public Page<Flats> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.flatsRepository.findAll(pageable);
    }
}
