package md.step.BlocAdmin.exception;

public class SuppliersNotFoundException extends EntityNotFoundException{
    public SuppliersNotFoundException(Integer id) {
        super("Suppliers", id);
    }
}
