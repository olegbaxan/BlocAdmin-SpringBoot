package md.step.BlocAdmin.exception;

public class InvalidOldPasswordException extends EntityNotFoundException {
    public InvalidOldPasswordException(Integer id) {
        super("Person", id);
    }
}
