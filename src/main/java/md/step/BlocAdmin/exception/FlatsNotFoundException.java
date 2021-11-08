package md.step.BlocAdmin.exception;

public class FlatsNotFoundException extends EntityNotFoundException {
    public FlatsNotFoundException(Integer id) {
        super("Flats", id);
    }
}
