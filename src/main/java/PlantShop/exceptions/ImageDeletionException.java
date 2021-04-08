package PlantShop.exceptions;

/**
 * Exception thrown when user tries to delete a plant and its image cannot be deleted.
 * @author Lea Ben Zvi
 */
public class ImageDeletionException extends LoginException {
    
    public ImageDeletionException() {
        super();
    }
    
    public ImageDeletionException(String message) {
        super(message);
    }
    
}