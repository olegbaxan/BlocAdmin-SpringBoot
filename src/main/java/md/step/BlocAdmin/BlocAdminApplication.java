package md.step.BlocAdmin;

import md.step.BlocAdmin.service.InitialisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;

@SpringBootApplication
public class BlocAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlocAdminApplication.class, args);
	}

	@Component
	class DemoCommandLineRunner implements CommandLineRunner {

		@Autowired
		private InitialisationService initialisationService;

		@Autowired
		private JavaMailSender javaMailSender;

		@Override
		public void run(String... args) throws Exception, MessagingException, IOException {
		//Setup DB initial data:Admin,Role,types,destinations,
		initialisationService.initializeAttribute();

		}

	}
}
