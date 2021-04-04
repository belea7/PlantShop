/*
 * Controller for plants catalog.
 */
package PlantShop.controller;


import PlantShop.model.PlantsModel;
import PlantShop.model.ShoppingCartModel;
import PlantShop.model.UserModel;
import PlantShop.entities.Plant;
import PlantShop.entities.Review;
import PlantShop.daos.ReviewsDao;
import PlantShop.exceptions.DaoException;
import PlantShop.view.MessagesView;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
/**
 * Controller for the index web page which displays all plants in store.
 * 
 * @author leagi
 */
@Named(value = "catalogController")
@ViewScoped
public class CatalogController implements Serializable{

    private Plant selectedPlant;        // The selected plant for viewing
    private Review selectedReview;      // The selected review for editing
    private boolean newReview;          // Should the review be added to DB
    
    @Inject
    private PlantsModel plantsModel;
    
    @Inject
    private ShoppingCartModel shoppingCartModel;
    
    @Inject
    private ReviewsDao reviewsDao;
    
    @Inject
    private UserModel userModel;
    
    @Inject
    private MessagesView messagesView;
    
    /**
     * Returns plants in store from plants controller.
     * 
     * @return the plants in the store
     */
    public ArrayList<Plant> getPlants() {
        return this.plantsModel.getPlants();
    }
    
    /**
     * Getter for selected plant for viewing attribute.
     * 
     * @return the plant selected for editing
     */
    public Plant getSelectedPlant() {
        return selectedPlant;
    }
    
    /**
     * Setter for selected plant for viewing attribute.
     * 
     * @param selectedPlant 
     */
    public void setSelectedPlant(Plant selectedPlant) {
        this.selectedPlant = selectedPlant;
    }
    
    /**
     * Is the review being edited a new review.
     * 
     * @return is the review a new one
     */
    public boolean isNewReview() {
        return newReview;
    }
    
    /**
     * Setter for newReview attribute.
     * 
     * @param newReview 
     */
    public void setNewReview(boolean newReview) {
        this.newReview = newReview;
    }

    /**
     * Getter for selected review for editing attribute.
     * 
     * @return the selected review
     */
    public Review getSelectedReview() {
        return selectedReview;
    }
    
    /**
     * Setter for selected review for editing attribute.
     * 
     * @param selectedReview 
     */
    public void setSelectedReview(Review selectedReview) {
        this.selectedReview = selectedReview;
    }
    
    /**
     * Checks if a review can be edited.
     * A review can be edited if a user is connected, and if the review
     * was written by him.
     * 
     * @param review
     * @return can be edited or not
     */
    public boolean canReviewBeEdited(Review review) {
        // If user is logged-in
        if (userModel.getUser() == null)
            return false;
        
        // If the logged-in user wrote the review
        return userModel.getUser().getUsername().equals(review.getUserName());
    }
    
    /**
     * Add the selected plant to the user's cart.
     * A plant can be added to cart if the user is logged-in and if the plant
     * is not already in it's cart.
     */
    public void addSelectedPlantToCart() {
        // If the user is not logged-in - ask user to log-in
        if (!userModel.isLoggedIn()) {
            String message = "Please log-in if you want to add a plant to cart";
            messagesView.displayErrorMessage(message);
            return;
        }
        
        // If plant is already in user's cart - notify user and don't add plant
        else if (shoppingCartModel.getCart().isPlantInCart(selectedPlant)) {
            String message = this.selectedPlant.getName() + " is already in your cart";
            messagesView.displayErrorMessage(message);
            return;
        }
        
        // Otherwise - add plant tp cart and notify user
        try {
            shoppingCartModel.addToCart(selectedPlant);
        } catch (DaoException e) {
            e.printStackTrace();
            messagesView.displayErrorMessage("Failed to add item to cart");
            return;
        }
        messagesView.displayInfoMessage("Plant was successfully added to cart");
        PrimeFaces.current().ajax().update("dt-products");
    }
    
    /**
     * Create a new review for editing.
     */
    public void createReview() {
        this.newReview = true;
        this.selectedReview = new Review();
        this.selectedReview.setPlant(this.selectedPlant);
        this.selectedReview.setUserName(userModel.getUser().getUsername());
    }
    
    /**
     * Returns if a new review can be added.
     * Only logged-in users can add reviews.
     * 
     * @return can be added or not
     */
    public boolean canReviewBeAdded() {
        return this.userModel.isLoggedIn();
    }
    
    /**
     * Save the state of the review being edited.
     */
    public void saveReview() {
        // If it is a new review - add it to the application.
        if (this.newReview) {
            this.selectedReview.setTimePosted(new Timestamp(System.currentTimeMillis()));
            try {
                reviewsDao.addReview(selectedReview);
            } catch (DaoException e) {
                e.printStackTrace();
                messagesView.displayErrorMessage("Failed to post review");
                return;
            }
            this.selectedPlant.addReview(selectedReview);
            this.newReview = false;
            messagesView.displayInfoMessage("Review successfully added");
        }
        // Otherwise - update the existing review.
        else {
            try {
                reviewsDao.updateReview(selectedReview);
            } catch (DaoException e) {
                e.printStackTrace();
                messagesView.displayErrorMessage("Failed to post review");
                return;
            }
            messagesView.displayInfoMessage("Review successfully updated");
        }
        PrimeFaces.current().ajax().update("body", "edit-review-content", "plantDetails", "plants");
        PrimeFaces.current().executeScript("PF('editReviewDialog').hide()");
    }
    
    /**
     * Removes the selected review from application.
     */
    public void removeSelectedReview() {
        selectedPlant.removeReview(selectedReview);
        try {
            reviewsDao.removeReview(this.selectedReview);
        } catch (DaoException e) {
            e.printStackTrace();
            messagesView.displayErrorMessage("Failed to remove review");
            return;
        }
        this.selectedReview = null;
        messagesView.displayInfoMessage("Review was removed");
    }
}
