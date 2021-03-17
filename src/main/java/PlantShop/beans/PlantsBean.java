/*
 * Plants bean.
 */
package PlantShop.beans;


import PlantShop.entities.Plant;
import PlantShop.daos.PlantsDao;
import PlantShop.daos.ReviewsDao;
import PlantShop.entities.Review;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;

/**
 * Bean for the index web page which displays all plants in store.
 * 
 * @author leagi
 */
@Named(value = "plantsBean")
@ViewScoped
public class PlantsBean implements Serializable{

    private ArrayList<Plant> plants;    // List of all plants in store
    private Plant selectedPlant;        // The selected plant for viewing
    private Review selectedReview;      // The selected review for editing
    private boolean newReview;          // Should the review be added to DB
    
    @Inject
    private PlantsDao dao;
    @Inject
    private ReviewsDao reviewsDao;
    
    /**
     * Fetches all the plants in the store.
     */
    @PostConstruct
    public void init() {
        plants = dao.getPlants();
    }
    
    /**
     * Getter for plants attribute.
     * 
     * @return the plants in the store
     */
    public ArrayList<Plant> getPlants() {
        return this.plants;
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
     * Create a new review for editing.
     */
    public void createReview() {
        this.newReview = true;
        this.selectedReview = new Review();
        this.selectedReview.setPlant(this.selectedPlant);
        this.selectedReview.setUserName("admin");
    }
    
    /**
     * Save the state of the review being edited.
     */
    public void saveReview() {
        // If it is a new review - add it to the application.
        if (this.newReview) {
            this.selectedReview.setTimePosted(new Timestamp(System.currentTimeMillis()));
            reviewsDao.addReview(selectedReview);
            this.newReview = false;
            this.selectedPlant.getReviews().add(selectedReview);
        }
        // Otherwise - update the existing review.
        else {
            reviewsDao.updateReview(selectedReview);
        }
    }
    
    /**
     * Removes the selected review for application.
     */
    public void removeSelectedReview() {
        this.selectedPlant.getReviews().remove(this.selectedReview);
        reviewsDao.removeReview(this.selectedReview);
        this.selectedReview = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Review Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:reviews");
    }
}
