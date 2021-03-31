package PlantShop.exceptions;

/**
 * Exception thrown when something went wrong with access to the database.
 * @author Ron Mosenzon
 */
public class DaoException extends Exception {
    
    public DaoException() {
        super();
    }
    
    public DaoException(String message) {
        super(message);
    }
    
}
