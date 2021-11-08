package md.step.BlocAdmin;

import md.step.BlocAdmin.model.*;
import md.step.BlocAdmin.repository.*;
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
		private StatusRepository statusRepository;
		@Autowired
		private MeterTypeRepository meterTypeRepository;
		@Autowired
		private TypeOfMeterAndInvoiceRepository typeOfMeterAndInvoiceRepository;

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

			Status statusNew = new Status();
			statusNew.setId(1);
			statusNew.setName(EStatus.STATUS_NEW);
			statusRepository.save(statusNew);
			Status statusPayed = new Status();
			statusPayed.setId(2);
			statusPayed.setName(EStatus.STATUS_PAYED);
			statusRepository.save(statusPayed);
			Status statusClosed = new Status();
			statusClosed.setId(3);
			statusClosed.setName(EStatus.STATUS_CLOSED);
			statusRepository.save(statusClosed);
			Status statusSendInvoice = new Status();
			statusSendInvoice.setId(3);
			statusSendInvoice.setName(EStatus.STATUS_SENDINVOICE);
			statusRepository.save(statusSendInvoice);
			MeterType typeWater = new MeterType();
			typeWater.setId(1);
			typeWater.setName(EMeterType.DEST_WATER);
			meterTypeRepository.save(typeWater);
			MeterType typeGas = new MeterType();
			typeGas.setId(2);
			typeGas.setName(EMeterType.DEST_GAS);
			meterTypeRepository.save(typeGas);
			MeterType typeElectricity = new MeterType();
			typeElectricity.setId(3);
			typeElectricity.setName(EMeterType.DEST_ELECTRICITY);
			meterTypeRepository.save(typeElectricity);
			MeterType typeGarbage = new MeterType();
			typeGarbage.setId(4);
			typeGarbage.setName(EMeterType.DEST_GARBAGE);
			meterTypeRepository.save(typeGarbage);
			MeterType typeClean = new MeterType();
			typeClean.setId(5);
			typeClean.setName(EMeterType.DEST_CLEAN);
			meterTypeRepository.save(typeClean);

			TypeOfMeterAndInvoice typeOFBuilding = new TypeOfMeterAndInvoice();
			typeOFBuilding.setId(1);
			typeOFBuilding.setName(ETypeOfMeterAndInvoice.TYPE_BUILDING);
			typeOfMeterAndInvoiceRepository.save(typeOFBuilding);
			TypeOfMeterAndInvoice typeOFLadder = new TypeOfMeterAndInvoice();
			typeOFLadder.setId(2);
			typeOFLadder.setName(ETypeOfMeterAndInvoice.TYPE_LADDER);
			typeOfMeterAndInvoiceRepository.save(typeOFLadder);
			TypeOfMeterAndInvoice typeOFFlats = new TypeOfMeterAndInvoice();
			typeOFFlats.setId(3);
			typeOFFlats.setName(ETypeOfMeterAndInvoice.TYPE_FLATS);
			typeOfMeterAndInvoiceRepository.save(typeOFFlats);


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
