package md.step.BlocAdmin.service;

import md.step.BlocAdmin.model.*;
import md.step.BlocAdmin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class InitialisationService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private MeterDestRepository meterDestRepository;
    @Autowired
    private TypeOfMeterInvoiceRepository typeOfMeterInvoiceRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    PasswordEncoder encoder;

    public void initializeAttribute() {
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
        statusSendInvoice.setId(4);
        statusSendInvoice.setName(EStatus.STATUS_SENDINVOICE);
        statusRepository.save(statusSendInvoice);

        MeterDest typeWater = new MeterDest();
        typeWater.setId(1);
        typeWater.setName(EMeterDest.DEST_WATER);
        meterDestRepository.save(typeWater);
        MeterDest typeGas = new MeterDest();
        typeGas.setId(2);
        typeGas.setName(EMeterDest.DEST_GAS);
        meterDestRepository.save(typeGas);
        MeterDest typeElectricity = new MeterDest();
        typeElectricity.setId(3);
        typeElectricity.setName(EMeterDest.DEST_ELECTRICITY);
        meterDestRepository.save(typeElectricity);
        MeterDest typeGarbage = new MeterDest();
        typeGarbage.setId(4);
        typeGarbage.setName(EMeterDest.DEST_GARBAGE);
        meterDestRepository.save(typeGarbage);
        MeterDest typeClean = new MeterDest();
        typeClean.setId(5);
        typeClean.setName(EMeterDest.DEST_CLEAN);
        meterDestRepository.save(typeClean);

        TypeOfMeterInvoice typeOFBuilding = new TypeOfMeterInvoice();
        typeOFBuilding.setId(1);
        typeOFBuilding.setName(ETypeOfMeterInvoice.TYPE_BUILDING);
        typeOfMeterInvoiceRepository.save(typeOFBuilding);
        TypeOfMeterInvoice typeOFLadder = new TypeOfMeterInvoice();
        typeOFLadder.setId(2);
        typeOFLadder.setName(ETypeOfMeterInvoice.TYPE_LADDER);
        typeOfMeterInvoiceRepository.save(typeOFLadder);
        TypeOfMeterInvoice typeOFFlats = new TypeOfMeterInvoice();
        typeOFFlats.setId(3);
        typeOFFlats.setName(ETypeOfMeterInvoice.TYPE_FLATS);
        typeOfMeterInvoiceRepository.save(typeOFFlats);
        TypeOfMeterInvoice typeOFPerson = new TypeOfMeterInvoice();
        typeOFPerson.setId(4);
        typeOFPerson.setName(ETypeOfMeterInvoice.TYPE_PERSON);
        typeOfMeterInvoiceRepository.save(typeOFPerson);

        if (!personRepository.existsByUsername("admin")) {
            Person admin = new Person();
            Set<Role> roles = new HashSet<>();
            admin.setUsername("admin");
            admin.setEmail("oleg.baxan@gmail.com");
            admin.setPassword(encoder.encode("admin123"));
            admin.setName("Oleg");
            admin.setSurname("Baxan");
            admin.setIdnp("1234567890123");
            admin.setMobile("067777777");
            admin.setDescription("Some text description");
            admin.setRegDate(LocalDate.now());
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
            admin.setRoles(roles);

            personRepository.save(admin);
        }
        if (!personRepository.existsByUsername("blocadmin")) {
            Person blocadmin = new Person();
            Set<Role> roles = new HashSet<>();
            blocadmin.setUsername("blocadmin");
            blocadmin.setEmail("blocadmin@gmail.com");
            blocadmin.setPassword(encoder.encode("blocadmin"));
            blocadmin.setName("blocadmin");
            blocadmin.setSurname("blocadmin");
            blocadmin.setIdnp("1234567890124");
            blocadmin.setMobile("067777775");
            blocadmin.setDescription("Building Administrator");
            blocadmin.setRegDate(LocalDate.now());
            Role blocadminRole = roleRepository.findByName(ERole.ROLE_BLOCADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(blocadminRole);
            blocadmin.setRoles(roles);

            personRepository.save(blocadmin);
        }
    }
}
