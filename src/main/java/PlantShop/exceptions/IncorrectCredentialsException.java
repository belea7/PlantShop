package PlantShop.exceptions;

/**
 * Exception thrown when user tries to log in with incorrect username/password.
 * @author Ron Mosenzon
 */
public class IncorrectCredentialsException extends LoginException {
    
    public IncorrectCredentialsException() {
        super();
    }
    
    public IncorrectCredentialsException(String message) {
        super(message);
    }
    
}