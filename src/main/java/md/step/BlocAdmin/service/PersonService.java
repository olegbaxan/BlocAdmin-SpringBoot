package md.step.BlocAdmin.service;

import md.step.BlocAdmin.exception.PersonNotFoundException;
import md.step.BlocAdmin.model.Person;
import md.step.BlocAdmin.model.Role;
import md.step.BlocAdmin.repository.PersonRepository;
import md.step.BlocAdmin.repository.RoleRepository;
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
public class PersonService {
    private final PersonRepository personRepository;
    private RoleRepository roleRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, RoleRepository roleRepository) {

        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
    }


    public Person addPerson(Person person) {
        return personRepository.save(person);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Set<Role> getRolesByPersonId(Integer id) {
        Optional<Person> person= personRepository.findById(id);
        Set<Role> roleList =person.get().getRoles();
        return roleList;
    }

    public Person updatePerson(Person person) {
        return personRepository.save(person);
    }


    public Person findPersonById(Integer id) throws PersonNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }

    public void deletePerson(Integer id) throws PersonNotFoundException {
        final Person person = this.personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
        personRepository.delete(person);
    }

    public Page<Person> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.personRepository.findAll(pageable);
    }

    public Boolean checkUsernameExist(String username) {
        return personRepository.existsByUsername(username);
    }
    public Boolean checkIdnpExist(String idnp) {
        return personRepository.existsByIdnp(idnp);
    }

    //Password forgotten

//    public void updateResetPasswordToken(String token, String email) throws PersonNotFoundException {
//        Person person = personRepository.findByEmail(email);
//        if (person != null) {
//            person.setResetPasswordToken(token);
//            personRepository.save(person);
//        } else {
//            throw new PersonNotFoundException(person.getPersonid());
//        }
//    }
//
//    public Person getByResetPasswordToken(String token) {
//        return personRepository.findByResetPasswordToken(token);
//    }
//
//    public void updatePassword(Person person, String newPassword) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode(newPassword);
//        person.setPassword(encodedPassword);
//
//        person.setResetPasswordToken(null);
//        personRepository.save(person);
//    }
}
