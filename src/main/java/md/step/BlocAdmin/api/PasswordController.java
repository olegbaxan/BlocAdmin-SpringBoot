package md.step.BlocAdmin.api;

import md.step.BlocAdmin.model.Person;
import md.step.BlocAdmin.security.services.EmailServiceImpl;
import md.step.BlocAdmin.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/password")
public class PasswordController {

    @Autowired
    private IPersonService personService;

    @Autowired
    private EmailServiceImpl emailService;

    //    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // Display forgotPassword page
    // @RequestMapping(value = "/forgot", method = RequestMethod.GET)
    // public ModelAndView displayForgotPasswordPage() {
    //     return new ModelAndView("forgotPassword");
    // }

    // Process form submission from forgotPassword page
    @PostMapping("/forgot")
    public ResponseEntity<?> processForgotPasswordForm(@Valid @RequestBody String personEmail, HttpServletRequest request) {

        // Lookup user in database by e-mail
        Optional<Person> optional = personService.findPersonByEmail(personEmail);
        String message;

        if (!optional.isPresent()) {
            message= "We didn't find an account for that e-mail address.";
        } else {

            // Generate random 36-character string token for reset password
            Person person = optional.get();
            person.setResetPasswordToken(UUID.randomUUID().toString());

            // Save token to database
            personService.save(person);

            String appUrl = request.getScheme() + "://" + request.getServerName();

            // Email message
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom("oleg.baxan@gmail.com");
            passwordResetEmail.setTo(person.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link below:\n" +appUrl+":4200/password/forgot/reset?token=" + person.getResetPasswordToken());
            System.out.println("Email text = "+ passwordResetEmail.getText());
            emailService.sendEmail(passwordResetEmail);

            // Add success message to view
            message="A password reset link has been sent to " + personEmail;
        }

        return ResponseEntity.ok(message);
    }

    // Display form to reset password
//    @RequestMapping(value = "/reset", method = RequestMethod.GET)
//    public ModelAndView displayResetPasswordPage(ModelAndView modelAndView, @RequestParam("token") String token) {
//
//        Optional<Person> person = personService.findPersonByResetPasswordToken(token);
//
//        if (person.isPresent()) { // Token found in DB
//            modelAndView.addObject("resetToken", token);
//        } else { // Token not found in DB
//            modelAndView.addObject("errorMessage", "Oops!  This is an invalid password reset link.");
//        }
//
//        modelAndView.setViewName("resetPassword");
//        return modelAndView;
//    }

    // Process reset password form
//    @PostMapping("/reset")
////    public ModelAndView setNewPassword(ModelAndView modelAndView, @RequestBody ResetPasswordRequest request, RedirectAttributes redir) {
//        public ResponseEntity<?> setNewPassword(@Valid @RequestBody String token, HttpServletRequest request) {
//        // Find the user associated with the reset token
//        Optional<Person> person = personService.findPersonByResetPasswordToken(requestParams.get("token"));
//
//        // This should always be non-null but we check just in case
//        if (person.isPresent()) {
//
//            Person resetPerson = person.get();
//
//            // Set new password
//            resetPerson.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password")));
//
//            // Set the reset token to null so it cannot be used again
//            resetPerson.setResetPasswordToken(null);
//
//            // Save user
//            personService.save(resetPerson);
//
//            // In order to set a model attribute on a redirect, we must use
//            // RedirectAttributes
//            redir.addFlashAttribute("successMessage", "You have successfully reset your password.  You may now login.");
//
//            modelAndView.setViewName("redirect:login");
//            return modelAndView;
//
//        } else {
//            modelAndView.addObject("errorMessage", "Oops!  This is an invalid password reset link.");
//            modelAndView.setViewName("resetPassword");
//        }
//
//        return modelAndView;
//    }

    // Going to reset page without a token redirects to login page
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex) {
        return new ModelAndView("redirect:login");
    }
}