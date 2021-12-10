package md.step.BlocAdmin.security.services;

import md.step.BlocAdmin.model.Person;
import md.step.BlocAdmin.repository.PersonRepository;
import md.step.BlocAdmin.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userService")
public class PersonServiceImpl implements IPersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public Optional findPersonByEmail(String email) {
        return personRepository.findPersonByEmail(email);
    }

    @Override
    public Optional findPersonByResetPasswordToken(String resetToken) {
        return personRepository.findPersonByResetPasswordToken(resetToken);
    }
    @Override
    public Optional findPersonByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    @Override
    public void save(Person person) {
        personRepository.save(person);
    }
}
