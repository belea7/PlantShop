/*
 * Class representing a plant in the shop.
 */
package PlantShop.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Plant class
 * @author leagi
 */
public class Plant implements Serializable{
    
    private int id;
    private String name;
    private int numberOfItems;
    private String light;
    private String water;
    private String fertilize;
    private String difficulty;
    private String description;
    private String picture;
    private int price;
    private ArrayList<Review> reviews;
    private int rating;
    
    public Plant() {
        reviews = new ArrayList();
        rating = 0;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

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
        return this.id == other.id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getFertilize() {
        return fertilize;
    }

    public void setFertilize(String fertilize) {
        this.fertilize = fertilize;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
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

    public int getRating() {
        return rating;
    }
}
