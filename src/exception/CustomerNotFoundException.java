package exception;

public class CustomerNotFoundException extends GarageException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}