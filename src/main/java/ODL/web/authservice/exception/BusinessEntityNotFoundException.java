package ODL.web.authservice.exception;

public class BusinessEntityNotFoundException extends NamedException {

    public BusinessEntityNotFoundException(String message) {
        super(message, BusinessEntityNotFoundException.class);
    }
}
