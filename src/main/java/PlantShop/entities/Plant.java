/*
 * Class representing a plant in the shop.
 */
package PlantShop.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Plant class
 * @author Lea Ben Zvi
 */
public class Plant implements Comparable<Plant>, Serializable {
    
    private long id;                        // ID of the plant
    private String name;                    // Name of the plant
    private int numberOfItems;              // Number of times in stock
    private String light;                   // Light conditions
    private String water;                   // Watering conditions
    private String fertilize;               // Fertilize conditions
    private String difficulty;              // Difficulty (easy/normal/difficult)
    private String description;             // Description of the plant
    private String picture;                 // Name of the image (in images dir)
    private double price;                    // The price of the plant
    private ArrayList<Review> reviews;      // The reviews on the plant
    private int rating;                     // The avg. rating of the plant
    
    /**
     * Constructor for Plant entity.
     */
    public Plant(){
        reviews = new ArrayList();
        rating = 0;
    }
    
    /**
     * Getter for plant ID.
     * 
     * @return plant ID
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for plant ID.
     * @param id 
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * Getter for plant name.
     * 
     * @return Name of the plant
     */
    public String getName() {
        return name;
    }
    
    /**
     * Setter for plant name.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Getter for number of items in stock.
     * 
     * @return number of items
     */
    public int getNumberOfItems() {
        return numberOfItems;
    }
    
    /**
     * Setter for number of items in stock.
     * 
     * @param numberOfItems 
     */
    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }
    
    /**
     * Getter for light conditions of the plant.
     * 
     * @return light conditions
     */
    public String getLight() {
        return light;
    }
    
    /**
     * Setter for light conditions of the plant.
     * 
     * @param light
     */
    public void setLight(String light) {
        this.light = light;
    }
    
    /**
     * Getter for water conditions of the plant.
     * 
     * @return water conditions
     */
    public String getWater() {
        return water;
    }
    /**
     * Setter for water conditions of the plant.
     * 
     * @param water 
     */
    public void setWater(String water) {
        this.water = water;
    }
    
    /**
     * Getter for fertilize conditions of the plant.
     * 
     * @return fertilize conditions
     */
    public String getFertilize() {
        return fertilize;
    }
    
    /**
     * Setter for fertilize conditions.
     * 
     * @param fertilize 
     */
    public void setFertilize(String fertilize) {
        this.fertilize = fertilize;
    }
    
    /**
     * Getter for difficulty of the plant.
     * 
     * @return the difficulty
     */
    public String getDifficulty() {
        return difficulty;
    }
    
    /**
     * Setter for difficulty of the plant.
     * 
     * @param difficulty 
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    /**
     * Getter for description of the plant.
     * 
     * @return the plant's description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Setter for descriptions of the plant.
     * 
     * @param description 
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Getter for picture name of the plant in the images dir.
     * 
     * @return the name of the picture file
     */
    public String getPicture() {
        return picture;
    }
    
    /**
     * Setter for picture name of the plant in the images dir.
     * 
     * @param picture 
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }
    
    /**
     * Getter for price of the plant.
     * 
     * @return the price
     */
    public double getPrice() {
        return price;
    }
    
    /**
     * Setter for price of the plant.
     * 
     * @param price 
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /**
     * Getter for reviews list of the plant.
     * 
     * @return list of reviews
     */
    public ArrayList<Review> getReviews() {
        return reviews;
    }
    
    /**
     * Setter for reviews list of the plant.
     * After the reviews list is set, the avg. rating of the plant is recalculated.
     * 
     * @param reviews 
     */
    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
        
        // If no review - the rating is 0
        if (reviews.isEmpty()) {
            this.rating = 0;
            return;
        }
        
        // Otherwise - calculate avg. rating
        updateRating();
    }
    
    /**
     * Adds a review on the plant and updates plant's rating.
     * 
     * @param review 
     */
    public void addReview(Review review) {
        this.reviews.add(review);
        updateRating();
    }
    
    /**
     * Removes a review on the plant and updates plant's rating.
     * 
     * @param review 
     */
    public void removeReview(Review review) {
        this.reviews.remove(review);
        updateRating();
    }
    
    /**
     * Updates a review on a plant and updates plant's rating.
     */
    public void updateRating() {
        if (reviews.isEmpty()) {
            this.rating = 0;
            return;
        } 
        
        int total = 0;
        for (Review r: reviews) {
            total += r.getRating();
        }
        this.rating = total / reviews.size();
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
     * Is the plant in stock (number of items available larger than 0)
     * 
     * @return in stock or not
     */
    public boolean getInStock() {
        return (numberOfItems > 0);
    }
    
    /**
     * Hash code function for plant object.
     * 
     * @return hash code of the plant object
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    /**
     * Compares two objects and returns it they are equal.
     * 
     * @param obj
     * @return equal or not
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Plant other = (Plant) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Plant other) {
        if (this == other) {
            return 0;
        }
        if (this.id < other.id) {
            return -1;
        }
        return 1;
    }
}