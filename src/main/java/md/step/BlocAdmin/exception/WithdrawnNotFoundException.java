package md.step.BlocAdmin.exception;

public class WithdrawnNotFoundException extends EntityNotFoundException {
    public WithdrawnNotFoundException(Integer id) {
        super("Withdrawn", id);
    }
}
