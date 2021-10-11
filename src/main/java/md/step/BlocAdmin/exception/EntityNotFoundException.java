package md.step.BlocAdmin.exception;

public class EntityNotFoundException extends Exception{

    public EntityNotFoundException(String entity, Integer id) {
        super(String.format("%s with id=%d does not exist", entity, id));
    }
}
