package PlantShop.exceptions;

/**
 * Exception representing incorrect format of profile details input.
 * @author Ron Mosenzon
 */
public class ProfileInputFormatException extends Exception {
    
    public ProfileInputFormatException() {
        super();
    }
    
    public ProfileInputFormatException(String message) {
        super(message);
    }
    
}