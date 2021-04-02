/*
 * Contians the ShoppingCart entity.
 */
package PlantShop.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a shopping cart of a user in the store.
 * 
 * @author leagi
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
            total += p.getPlant().getPrice();
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
        this.totalPrice += plant.getPlant().getPrice();
    }
    
    /**
     * Remove plant from user's cart.
     * Updates the total cost of the items in the cart.
     * 
     * @param plant 
     */
    public void removeFromCart(PlantInCart plant) {
        this.plantsInCart.remove(plant);
        this.totalPrice -= plant.getPlant().getPrice();
    }
}