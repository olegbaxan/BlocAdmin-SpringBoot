package md.step.BlocAdmin.exception;

public class MetersNotFoundException extends EntityNotFoundException{
    public MetersNotFoundException(Integer id) {
        super("Meters", id);
    }
}
