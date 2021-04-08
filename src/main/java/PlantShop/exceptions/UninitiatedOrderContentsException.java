package PlantShop.exceptions;

/**
 * An exception thrown when attempting to retrieve the plants list or total cost
 * of an Order object whose plants list has not been initiated.
 * @author Ron Mosenzon
 */
public class UninitiatedOrderContentsException extends RuntimeException {
    
    public UninitiatedOrderContentsException() {
        super();
    }
    
    public UninitiatedOrderContentsException(String message) {
        super(message);
    }
    
}