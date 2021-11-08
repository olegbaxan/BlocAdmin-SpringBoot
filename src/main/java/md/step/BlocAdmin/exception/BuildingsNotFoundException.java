package md.step.BlocAdmin.exception;

public class BuildingsNotFoundException extends EntityNotFoundException{
    public BuildingsNotFoundException(Integer id) {
        super("Buildings", id);
    }
}
