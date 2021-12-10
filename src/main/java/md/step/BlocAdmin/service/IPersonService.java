package md.step.BlocAdmin.service;

import md.step.BlocAdmin.model.Person;

import java.util.Optional;

    public interface IPersonService {
        public Optional<Person> findPersonByEmail(String email);
        public Optional<Person> findPersonByUsername(String username);
        public Optional<Person> findPersonByResetPasswordToken(String resetToken);
        public void save(Person person);
    }

