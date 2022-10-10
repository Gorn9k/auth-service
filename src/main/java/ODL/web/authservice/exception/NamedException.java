package ODL.web.authservice.exception;

public class NamedException extends RuntimeException {

    private final String name;

    NamedException(String message, Class<? extends Exception> clazz) {
        super(message);
        this.name = clazz.getSimpleName();
    }

    public String getName() {
        return name;
    }
}
