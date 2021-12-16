package md.step.BlocAdmin.api;

import md.step.BlocAdmin.model.Person;
import md.step.BlocAdmin.payload.request.ChangePasswordRequest;
import md.step.BlocAdmin.payload.request.ResetPasswordRequest;
import md.step.BlocAdmin.security.services.EmailServiceImpl;
import md.step.BlocAdmin.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(maxAge = 3600, allowCredentials = "true", origins = "https://blocadmin-angularui.herokuapp.com/")
@RestController
@RequestMapping("/api/v1/password")
public class PasswordController {

    @Autowired
    private IPersonService personService;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    PasswordEncoder encoder;


    // Process form submission from forgotPassword page
    @PostMapping("/forgot")
    public ResponseEntity<?> processForgotPasswordForm(@Valid @RequestBody String personEmail, HttpServletRequest request) {

        //String baseUrl = String.format("%s://%s:%d/tasks/",request.getScheme(),  request.getServerName(), request.getServerPort());

        // Lookup user in database by e-mail
        Optional<Person> optional = personService.findPersonByEmail(personEmail);
        String message;

        if (!optional.isPresent()) {
            message = "We didn't find an account for that e-mail address.";
        } else {

            // Generate random 36-character string token for reset password
            Person person = optional.get();
            person.setResetPasswordToken(UUID.randomUUID().toString());

            // Email message
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom("oleg.baxan.test@gmail.com");
            passwordResetEmail.setTo(person.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link below:\nhttps://blocadmin-angularui.herokuapp.com/password/forgot/reset?token=" + person.getResetPasswordToken());
            emailService.sendEmail(passwordResetEmail);

            // Add success message to view
            message = "A password reset link has been sent to " + personEmail;
        }

        return ResponseEntity.ok(message);
    }


    // Process reset password form
    @PostMapping("/reset")
//    public ModelAndView setNewPassword(ModelAndView modelAndView, @RequestBody ResetPasswordRequest request, RedirectAttributes redir) {
    public ResponseEntity<?> setNewPassword(@Valid @RequestBody ResetPasswordRequest request) {
        // Find the user associated with the reset token
        Optional<Person> person = personService.findPersonByResetPasswordToken(request.getToken());
        String message = null;
        // This should always be non-null but we check just in case
        if (person.isPresent()) {

            Person resetPerson = person.get();

            // Set new password
            resetPerson.setPassword(encoder.encode(request.getPassword()));


            // Set the reset token to null so it cannot be used again
            resetPerson.setResetPasswordToken(null);

            // Save user
            personService.save(resetPerson);

            // In order to set a model attribute on a redirect, we must use
            // RedirectAttributes
            return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.OK);

        } else {
            return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
    }

    // Process change password form
    @PostMapping("/changepassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        // Find the user associated with the reset token
        Optional<Person> person = personService.findPersonByUsername(request.getUsername());
        String message = null;
        // This should always be non-null but we check just in case
//        try {
        if (person.isPresent()) {

            Person changePerson = person.get();
            //Check for old password
            if (!encoder.matches(request.getOldpassword(), person.get().getPassword())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            // Set new password
            changePerson.setPassword(encoder.encode(request.getNewpassword()));
            // Save user
            personService.save(changePerson);

            return ResponseEntity.status(HttpStatus.OK).build();

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }
}