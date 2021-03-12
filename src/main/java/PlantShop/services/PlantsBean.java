/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlantShop.services;


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
 *
 * @author leagi
 */
@Named(value = "plantsBean")
@ViewScoped
@ManagedBean
public class PlantsBean implements Serializable{

    private ArrayList<Plant> plants;
    private ArrayList<Plant> selectedPlants;
    private Plant selectedPlant;
    private boolean newPlant;
    private boolean newReview;
    private Review selectedReview;
    
    @Inject
    private PlantsDao dao;
    @Inject
    private ReviewsDao reviewsDao;
    
    @PostConstruct
    public void init() {
        plants = dao.getPlants();
    }
    
    public ArrayList<Plant> getPlants() {
        return this.plants;
    }
    
        public ArrayList<Plant> getSelectedPlants() {
        return selectedPlants;
    }
    
    public void setSelectedPlants(ArrayList<Plant> selectedPlants) {
        this.selectedPlants = selectedPlants;
    }

    public Plant getSelectedPlant() {
        return selectedPlant;
    }

    public void setSelectedPlant(Plant selectedPlant) {
        this.selectedPlant = selectedPlant;
    }

    public boolean isNewPlant() {
        return newPlant;
    }

    public void setNewPlant(boolean newPlant) {
        this.newPlant = newPlant;
    }

    public boolean isNewReview() {
        return newReview;
    }

    public void setNewReview(boolean newReview) {
        this.newReview = newReview;
    }

    public Review getSelectedReview() {
        return selectedReview;
    }

    public void setSelectedReview(Review selectedReview) {
        this.selectedReview = selectedReview;
    }
    
    public void createPlant() {
        this.selectedPlant = new Plant();
        this.selectedPlant.setId(plants.size() + 1);
    }
    
    public void removeSelectedPlant() {
        this.plants.remove(this.selectedPlant);
        dao.removePlant(this.selectedPlant);
        this.selectedPlant = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Plant Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-plants");
    }
    
    public void removeSelectedPlants() {
        this.plants.removeAll(this.selectedPlants);
        dao.removePlants(this.selectedPlants);
        this.selectedPlants = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Plant Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-plants");
        PrimeFaces.current().executeScript("PF('dtPlants').clearFilters()");
    }
    
    public boolean hasSelectedPlants() {
        return this.selectedPlants != null && !this.selectedPlants.isEmpty();
    }
    
    public String getDeleteButtonMessage() {
        if (hasSelectedPlants()) {
            int size = this.selectedPlants.size();
            return size > 1 ? size + " plants selected" : "1 plant selected";
        }

        return "Delete";
    }
    
    public void save() {
        if (this.newPlant) {
            dao.addPlant(selectedPlant);
            this.newPlant = false;
            this.plants.add(selectedPlant);
        }
        else {
            dao.updatePlant(selectedPlant);
        }
    }
    
    public void createReview() {
        this.newReview = true;
        this.selectedReview = new Review();
        this.selectedReview.setTimePosted(new Timestamp(System.currentTimeMillis()));
        this.selectedReview.setPlantId(this.selectedPlant.getId());
        System.out.println(selectedReview);
    }
    
    public void saveReview() {
        if (this.newReview) {
            reviewsDao.addReview(selectedReview);
            this.newReview = false;
            this.selectedPlant.getReviews().add(selectedReview);
        }
        else {
            reviewsDao.updateReview(selectedReview);
        }
    }
    
    public void removeSelectedReview() {
        this.selectedPlant.getReviews().remove(this.selectedReview);
        reviewsDao.removeReview(this.selectedReview);
        this.selectedReview = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Review Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:reviews");
    }
}
