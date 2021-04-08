package PlantShop.exceptions;

/**
 * An exception thrown when an inconsistency in the database is detected.
 * @author Ron Mosenzon
 */
public class DatabaseInconsistencyException extends RuntimeException {
    
    public DatabaseInconsistencyException() {
        super();
    }
    
    public DatabaseInconsistencyException(String message) {
        super(message);
    }
    
}