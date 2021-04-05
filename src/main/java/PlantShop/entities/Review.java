/*
 * Entity for Review object.
 */
package PlantShop.entities;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *  Class representing a review for a plant in the store.
 
 * @author Lea Ben Zvi
 */
public class Review implements Serializable {
    
    private String userName;            // User who wrote the review
    private Plant plant;                // The ID of the plant reviewd
    private Timestamp timePosted;       // When the review was posted
    private int rating;                 // Rating given in the review
    private String comment = "";        // The comment in the review
    
    /**
     * Getter for userName attribute.
     * 
     * @return user name
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * Setter for userName attribute.
     * 
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /**
     * Getter for plant attribute.
     * 
     * @return the plant
     */
    public Plant getPlant() {
        return plant;
    }
    
    /**
     * Setter for plant attribute.
     * 
     * @param plant 
     */
    public void setPlant(Plant plant) {
        this.plant = plant;
    }
    
    /**
     * Getter for the time the review was posted.
     * 
     * @return the time posted
     */
    public Timestamp getTimePosted() {
        return timePosted;
    }
    
    /**
     * Setter for the time the review was posted.
     * 
     * @param timePosted 
     */
    public void setTimePosted(Timestamp timePosted) {
        this.timePosted = timePosted;
    }
    
    /**
     * Getter for the rating of the plant.
     * 
     * @return the rating of the plant
     */
    public int getRating() {
        return rating;
    }
    
    /**
     * Setter for the rating of the plant.
     * 
     * @param rating 
     */
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    /**
     * Getter for the comment of the review.
     * 
     * @return the comment
     */
    public String getComment() {
        return comment;
    }
    
    /**
     * Setter for the comment of the review.
     * 
     * @param comment 
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}