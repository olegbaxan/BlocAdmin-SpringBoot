package md.step.BlocAdmin.exception;


public class AddressNotFoundException extends EntityNotFoundException{
    public AddressNotFoundException(Integer id) {
        super("Address", id);
    }
}
