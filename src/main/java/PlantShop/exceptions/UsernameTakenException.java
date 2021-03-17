package PlantShop.exceptions;

/**
 * Exception thrown when a user tries to register with an already taken username.
 * @author Ron Mosenozn
 */
public class UsernameTakenException extends LoginException {
    
    public UsernameTakenException() {
        super();
    }
    
    public UsernameTakenException(String message) {
        super(message);
    }
    
}
