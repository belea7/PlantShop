/*
 * Class representing a plant in the shop.
 */
package PlantShop.entities;

import java.util.ArrayList;

/**
 * Plant class
 * @author leagi
 */
public class Plant{
    
    private int id;                         // ID of the plant
    private String name;                    // Name of the plant
    private int numberOfItems;              // Number of times in stock
    private String light;                   // Light conditions
    private String water;                   // Watering conditions
    private String fertilize;               // Fertilize conditions
    private String difficulty;              // Difficulty (easy/normal/difficult)
    private String description;             // Description of the plant
    private String picture;                 // Name of the image (in images dir)
    private int price;                      // The price of the plant
    private ArrayList<Review> reviews;      // The reviews on the plant
    private int rating;                     // The avg. rating of the plant
    
    /**
     * Constructor for Plant entity.
     */
    public Plant() {
        reviews = new ArrayList();
        rating = 0;
    }
    
    /**
     * Getter for plant ID.
     * 
     * @return plant ID
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for plant ID.
     * @param id 
     */
    public void setId(int id) {
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
    public int getPrice() {
        return price;
    }
    
    /**
     * Setter for price of the plant.
     * 
     * @param price 
     */
    public void setPrice(int price) {
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
}