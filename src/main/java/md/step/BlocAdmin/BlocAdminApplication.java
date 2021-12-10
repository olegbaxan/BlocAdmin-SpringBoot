package md.step.BlocAdmin;

import md.step.BlocAdmin.service.InitialisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;

@SpringBootApplication
public class BlocAdminApplication {
//public class BlocAdminApplication extends SpringBootServletInitializer {

	//https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto.traditional-deployment.war
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(BlocAdminApplication.class);
//	}

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
			//Mail config
//			EmailService.sendMail("oleg.baxan@gmail.com", "Hi", "Ho ho ho");
//			EmailService.sendPreConfiguredMail("Ho ho ho");
//			System.out.println("Sending Email...");

//			sendEmail();
			//sendEmailWithAttachment();

//			System.out.println("Done");




		}
		void sendEmail() {

			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo("oleg.baxan@gmail.com");

			msg.setSubject("Testing from Spring Boot");
			msg.setText("Hello World \n Spring Boot Email");

			javaMailSender.send(msg);

		}
	}
}
