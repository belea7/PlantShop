/*
 * Bean for plants catalog.
 */
package PlantShop.models;


import PlantShop.controllers.PlantsController;
import PlantShop.controllers.ShoppingCartController;
import PlantShop.controllers.UserBean;
import PlantShop.entities.Plant;
import PlantShop.entities.Review;
import PlantShop.daos.ReviewsDao;
import PlantShop.exceptions.DaoException;
import PlantShop.view.CatalogViewBean;
import PlantShop.view.ReviewsEditViewBean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
/**
 * Bean for the index web page which displays all plants in store.
 * 
 * @author leagi
 */
@Named(value = "catalogBean")
@ViewScoped
public class CatalogBean implements Serializable{

    private Plant selectedPlant;        // The selected plant for viewing
    private Review selectedReview;      // The selected review for editing
    private boolean newReview;          // Should the review be added to DB
    
    @Inject
    private PlantsController plantsController;
    
    @Inject
    private ShoppingCartController shoppingCartController;
    
    @Inject
    private ReviewsDao reviewsDao;
    
    @Inject
    private UserBean userBean;
    
    @Inject
    ReviewsEditViewBean reviewViewBean;
    
    @Inject
    private CatalogViewBean viewBean;
    
    /**
     * Returns plants in store from plants controller.
     * 
     * @return the plants in the store
     */
    public ArrayList<Plant> getPlants() {
        return this.plantsController.getPlants();
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
     * Is the review being edited is a new review.
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
        if (userBean.getUser() == null)
            return false;
        
        // If the logged-in user wrote the review
        return userBean.getUser().getUsername().equals(review.getUserName());
    }
    
    /**
     * Add the selected plant to the user's cart.
     * A plant can be added to cart if the user is logged-in and if the plant
     * is not already in it's cart.
     */
    public void addSelectedPlantToCart() {
        // If the user is not logged-in - ask uer to log-in
        if (!userBean.isLoggedIn()) {
            String message = "Please log-in if you want to add a plant to cart";
            viewBean.displayFormSubmissionErrorMessage(message);
            return;
        }
        
        // If user plant is already in user's count - notify user and don't remove plant
        else if (shoppingCartController.getCart().isPlantInCart(selectedPlant)) {
            String message = this.selectedPlant.getName() + " is already in your cart";
            viewBean.displayFormSubmissionErrorMessage(message);
            return;
        }
        
        // Otherwise - add plant tp cart and notify user
        try {
            shoppingCartController.addToCart(selectedPlant);
        } catch (DaoException e) {
            e.printStackTrace();
            viewBean.displayFormSubmissionErrorMessage("Failed to add item to cart");
        }
        viewBean.displayFormSubmissionInfoMessage("Plant was successfully added to cart");
    }
    
    /**
     * Create a new review for editing.
     */
    public void createReview() {
        this.newReview = true;
        this.selectedReview = new Review();
        this.selectedReview.setPlant(this.selectedPlant);
        this.selectedReview.setUserName(userBean.getUser().getUsername());
    }
    
    /**
     * Returns if a new review can be added.
     * Only logged-in users can add reviews.
     * 
     * @return can be added or not
     */
    public boolean canReviewBeAdded() {
        return this.userBean.isLoggedIn();
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
                viewBean.displayFormSubmissionErrorMessage("Failed to post review");
            }
            this.selectedPlant.addReview(selectedReview);
            this.newReview = false;
            reviewViewBean.displayFormSubmissionInfoMessage("Review successfully added");
        }
        // Otherwise - update the existing review.
        else {
            try {
                reviewsDao.updateReview(selectedReview);
            } catch (DaoException e) {
                e.printStackTrace();
                viewBean.displayFormSubmissionErrorMessage("Failed to post review");
            }
            reviewViewBean.displayFormSubmissionInfoMessage("Review successfully updated");
        }
        PrimeFaces.current().ajax().update("body", "edit-review-content", "plantDetails", "plants");
        PrimeFaces.current().executeScript("PF('editReviewDialog').hide()");
    }
    
    /**
     * Removes the selected review for application.
     */
    public void removeSelectedReview() {
        selectedPlant.removeReview(selectedReview);
        try {
            reviewsDao.removeReview(this.selectedReview);
        } catch (DaoException e) {
            e.printStackTrace();
            viewBean.displayFormSubmissionErrorMessage("Failed to remove review");
        }
        this.selectedReview = null;
        reviewViewBean.displayFormSubmissionInfoMessage("Review was removed");
    }
}
