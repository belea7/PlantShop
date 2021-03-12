/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlantShop.services;

import PlantShop.entities.Plant;
import PlantShop.entities.Review;
import PlantShop.daos.ReviewsDao;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author leagi
 */
@Named(value = "reviewsBean")
@ViewScoped
@ManagedBean
public class ReviewsBean implements Serializable{
    private Plant plant;
    private ArrayList<Review> reviews;
    private Review selectedReview;
    private boolean newReview;
    
    @Inject
    private ReviewsDao dao;
    
    @PostConstruct
    public void init() {
        reviews = new ArrayList();
    }
    
    public ArrayList<Review> getReviews() {
        System.out.println(reviews.size());
        return this.reviews;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
        reviews = dao.getReviews(plant);
    }

    public Review getSelectedReview() {
        return selectedReview;
    }

    public void setSelectedReview(Review selectedReview) {
        this.selectedReview = selectedReview;
    }
    
    public void createReview() {
        this.selectedReview = new Review();
    }
    
    public void removeSelectedReview() {
        this.reviews.remove(this.selectedReview);
        dao.removeReview(this.selectedReview);
        this.selectedReview = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Plant Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-reviews");
    }
    
    public void save() {
        if (this.newReview) {
            dao.addReview(selectedReview);
            this.newReview = false;
            this.reviews.add(selectedReview);
        }
        else {
            dao.updateReview(selectedReview);
        }
    }
}
