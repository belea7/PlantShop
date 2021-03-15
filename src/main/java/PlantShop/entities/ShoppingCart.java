/*
 * Contians the ShoppingCart entity.
 */
package PlantShop.entities;

import java.util.ArrayList;

/**
 * Represents a shopping cart of a user in the store.
 * 
 * @author leagi
 */
public class ShoppingCart {
    private ArrayList<PlantInCart> items;       // List of items in the cart
    private int totalPrice;                     // The total price of items in the cart
    
    /**
     * Constructor for shopping cart entity.
     */
    public ShoppingCart() {
        items = new ArrayList();
        totalPrice = 0;
    }
    
    /**
     * Getter for items list attribute.
     * 
     * @return list of items in cart
     */
    public ArrayList<PlantInCart> getItems() {
        return items;
    }
    
    /**
     * Setter for items list attribute.
     * Updates the total cost of the items in the cart.
     * 
     * @param items 
     */
    public void setItems(ArrayList<PlantInCart> items) {
        this.items = items;
        
        // Update the total price of the cart
        int total = 0;
        for (PlantInCart p: items) {
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
        for (PlantInCart p: items) {
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
        this.items.add(plant);
        this.totalPrice += plant.getPlant().getPrice();
    }
    
    /**
     * Remove plant from user's cart.
     * Updates the total cost of the items in the cart.
     * 
     * @param plant 
     */
    public void removeFromCart(PlantInCart plant) {
        this.items.remove(plant);
        this.totalPrice -= plant.getPlant().getPrice();
    }
}
