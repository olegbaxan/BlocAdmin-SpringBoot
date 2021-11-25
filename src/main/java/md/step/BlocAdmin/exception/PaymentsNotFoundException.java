package md.step.BlocAdmin.exception;

public class PaymentsNotFoundException extends EntityNotFoundException {
    public PaymentsNotFoundException(Integer id) {
        super("Payments", id);
    }
}
