/*
 * Contians the ShoppingCart entity.
 */
package PlantShop.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a shopping cart of a user in the store.
 * 
 * @author Lea Ben Zvi
 */
public class ShoppingCart implements Serializable {
    
    private User user;                              // The user whose cart it is
    private ArrayList<PlantInCart> plantsInCart;    // List of items in the cart
    private double totalPrice;                      // The total price of items in the cart
    
    /**
     * Constructor for shopping cart entity.
     * 
     * @param user
     */
    public ShoppingCart(User user) {
        this.user = user;
        this.plantsInCart = new ArrayList();
        this.totalPrice = 0;
    }
    
    /**
     * Getter for user attribute.
     * 
     * @return user
     */
    public User getUser() {
        return user;
    }
    
    /**
     * Setter for user attribute.
     * 
     * @param user 
     */
    public void setUser(User user) {
        this.user = user;
    }
    
    /**
     * Getter for total price attribute.
     * @return the total price of everything in this cart
     */
    public double getTotalPrice() {
        return totalPrice;
    }
    
    /**
     * Getter for items list attribute.
     * 
     * @return list of items in cart
     */
    public ArrayList<PlantInCart> getPlantsInCart() {
        return plantsInCart;
    }
    
    /**
     * Setter for items list attribute.Updates the total cost of the items in the cart.
     * 
     * @param plantsInCart
     */
    public void setItems(ArrayList<PlantInCart> plantsInCart) {
        this.plantsInCart = plantsInCart;
        
        // Update the total price of the cart
        double total = 0;
        for (PlantInCart p: plantsInCart) {
            total += p.getPlant().getPrice() * p.getAmount();
        }
        this.totalPrice = total;
    }
    
    /**
     * Checks if a plant is in the cart.
     * 
     * @param plant
     * @return is the plant in the cart
     */
    public boolean isPlantInCart(Plant plant) {
        // Loop through all items in the cart and compare to the plant
        for (PlantInCart p: plantsInCart) {
            if (p.getPlant().equals(plant))
                return true;
        }
        return false;
    }
    
    /**
     * Add plant to user's cart.
     * Updates the total cost of the items in the cart.
     * 
     * @param plant 
     */
    public void addToCart(PlantInCart plant) {
        this.plantsInCart.add(plant);
        double price = plant.getPlant().getPrice() * plant.getAmount();
        this.totalPrice += price;
    }
    
    /**
     * Remove plant from user's cart.
     * Updates the total cost of the items in the cart.
     * 
     * @param plant 
     */
    public void removeFromCart(PlantInCart plant) {
        this.plantsInCart.remove(plant);
        double price = plant.getPlant().getPrice() * plant.getAmount();
        this.totalPrice -= price;
    }
    
    /**
     * Increases amount of a plant in cart (if plant is in the cart).
     * @param plant 
     */
    public void increaseAmount(PlantInCart plant) {
        if (plantsInCart.isEmpty())
            return;
        if (plantsInCart.contains(plant)) {
            plant.increaseAmount();
            totalPrice += plant.getPlant().getPrice();
        }
    }
    
    /**
     * Decreases amount of a plant in cart (if plant is in the cart).
     * @param plant 
     */
    public void decreaseAmount(PlantInCart plant) {
        if (plantsInCart.isEmpty())
            return;
        if (plantsInCart.contains(plant)) {
            plant.decreaseAmount();
            totalPrice -= plant.getPlant().getPrice();
        }
    }
}