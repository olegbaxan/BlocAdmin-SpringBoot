package md.step.BlocAdmin.exception;

public class MeterDataNotFoundException extends EntityNotFoundException{
    public MeterDataNotFoundException(Integer id) {
        super("MeterData", id);
    }
}
