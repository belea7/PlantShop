package PlantShop.util;

/**
 * A class, usually a bean, that can display errors to the user.
 * @author Ron Mosenzon
 */
public interface ErrorDisplay {
    
    /**
     * Display an error to the user.
     * @param message the error message to be displayed.
     */
    public void displayErrorMessage(String message);
    
}
