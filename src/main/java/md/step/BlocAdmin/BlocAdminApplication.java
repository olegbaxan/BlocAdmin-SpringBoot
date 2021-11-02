package md.step.BlocAdmin;

import md.step.BlocAdmin.model.ERole;
import md.step.BlocAdmin.model.Person;
import md.step.BlocAdmin.model.Role;
import md.step.BlocAdmin.repository.PersonRepository;
import md.step.BlocAdmin.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BlocAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlocAdminApplication.class, args);
	}

	@Component
	class DemoCommandLineRunner implements CommandLineRunner {

		@Autowired
		private RoleRepository roleRepository;

		@Autowired
		private PersonRepository personRepository;

		@Autowired
		PasswordEncoder encoder;

		@Override
		public void run(String... args) throws Exception {

			Role roleAdmin = new Role();
			roleAdmin.setId(1);
			roleAdmin.setName(ERole.ROLE_ADMIN);
			roleRepository.save(roleAdmin);

			Role roleBlocAdmin = new Role();
			roleBlocAdmin.setId(2);
			roleBlocAdmin.setName(ERole.ROLE_BLOCADMIN);
			roleRepository.save(roleBlocAdmin);
			
			Role roleUser = new Role();
			roleUser.setId(3);
			roleUser.setName(ERole.ROLE_USER);
			roleRepository.save(roleUser);

			Person admin=new Person();
			Set<Role> roles = new HashSet<>();
			admin.setUsername("oleg");
			admin.setEmail("oleg@mail.md");
			admin.setPassword(encoder.encode("oleg12"));
			admin.setName("oleg");
			admin.setSurname("oleg");
			admin.setIdnp("1234567890123");
			admin.setMobile("067777777");
			admin.setDescription("Some text description");
			Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(adminRole);
			admin.setRoles(roles);

			personRepository.save(admin);

		}
	}
}
