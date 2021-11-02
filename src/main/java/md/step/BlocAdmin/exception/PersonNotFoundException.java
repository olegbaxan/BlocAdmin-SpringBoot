package md.step.BlocAdmin.exception;

public class PersonNotFoundException extends EntityNotFoundException{
    public PersonNotFoundException(Integer id) {
        super("Person", id);
    }
}
