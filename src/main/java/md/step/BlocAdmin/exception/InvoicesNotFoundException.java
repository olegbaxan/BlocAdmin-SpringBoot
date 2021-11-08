package md.step.BlocAdmin.exception;

public class InvoicesNotFoundException extends EntityNotFoundException{
    public InvoicesNotFoundException(Integer id) {
        super("Invoices", id);
    }
}
