package PlantShop.exceptions;

/**
 * An exception thrown when login/registration fails.
 * @author Ron Mosenzon
 */
public class LoginException extends Exception {
    
    public LoginException() {
        super();
    }
    
    public LoginException(String message) {
        super(message);
    }
    
}