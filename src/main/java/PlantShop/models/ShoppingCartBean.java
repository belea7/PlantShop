/*
 * Shopping cart bean.
 */
package PlantShop.models;

import PlantShop.entities.Plant;
import PlantShop.entities.PlantInCart;
import PlantShop.controllers.ShoppingCartController;
import PlantShop.view.ShoppingCartViewBean;

import java.io.Serializable;
import java.util.ArrayList;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 * Bean for managing shopping cart page.
 * 
 * @author leagi
 */
@Named(value = "shoppingCartBean")
@ViewScoped
public class ShoppingCartBean implements Serializable{
    
    private ArrayList<PlantInCart> selectedPlants;      // The selected plants from cart for removal
    private PlantInCart selectedPlant;                  // The selected plant for editing
    private Plant addedPlant;                           // A plant that should be added to the cart
    
    @Inject
    private ShoppingCartController controller;
    
    @Inject
    private ShoppingCartViewBean viewBean;

    /**
     * Returns plants in cart from shopping cart controller.
     * 
     * @return list of plants
     */
    public ArrayList<PlantInCart> getPlants() {
        return controller.getCart().getPlantsInCart();
    }
    
    /**
     * Returns total cart's price from shopping cart controller.
     * 
     * @return total price
     */
    public int getTotalPrice() {
        return controller.getCart().getTotalPrice();
    }
    
    /**
     * Getter for plant selected for editing the amount or removal.
     * 
     * @return the plants selected
     */
    public PlantInCart getSelectedPlant() {
        return selectedPlant;
    }
    
    /**
     * Setter for plant selected for editing the amount or removal.
     * 
     * @param selectedPlant 
     */
    public void setSelectedPlant(PlantInCart selectedPlant) {
        this.selectedPlant = selectedPlant;
    }
    
    /**
     * Getter for the plant to be added to the cart.
     * 
     * @return the plant to be added
     */
    public Plant getAddedPlant() {
        return addedPlant;
    }
    
    /**
     * Setter for the plant to be added to the cart.
     * 
     * @param addedPlant 
     */
    public void setAddedPlant(Plant addedPlant) {
        this.addedPlant = addedPlant;
    }
    
    /**
     * Getter for selected plants for removal from cart.
     * 
     * @return plants selected for removal
     */
    public ArrayList<PlantInCart> getSelectedPlants() {
        return selectedPlants;
    }
    
    /**
     * Setter for selected plants for removal from cart.
     * 
     * @param selectedPlants 
     */
    public void setSelectedPlants(ArrayList<PlantInCart> selectedPlants) {
        this.selectedPlants = selectedPlants;
    }
    
    /**
     * Remove selected plant from cart and notify user.
     */
    public void removeSelectedPlantFromCart() {
        controller.removeFromCart(selectedPlant);
        String name = this.selectedPlant.getPlant().getName();
        viewBean.displayObjectDeletionMessage(name + "was removed from cart");
        this.selectedPlant = null;
    }
    
    /**
     * Remove group of selected plants from cart and notify user.
     */
    public void removeSelectedPlants() {
        for (PlantInCart plant: this.selectedPlants)
            controller.removeFromCart(plant);
        viewBean.displayObjectDeletionMessage("Plants removed from cart");
         this.selectedPlants = null;
    }
    
    /**
     * Are there any plants selected for removal from cart.
     * 
     * @return are there ant plants in group
     */
    public boolean hasSelectedPlants() {
        return this.selectedPlants != null && !this.selectedPlants.isEmpty();
    }
    
    /**
     * Returns the value of the deletion button.
     * 
     * @return the message displayed
     */
    public String getDeleteButtonMessage() {
        // If any plant is selected
        if (hasSelectedPlants()) {
            int size = this.selectedPlants.size();
            return size > 1 ? size + " plants selected" : "1 plant selected";
        }
        
        // Otherwise
        return "Delete";
    }
    
    /**
     * Increase the amount of a plant in the cart.
     * Number of items in cart cannot be higher that number of items in stock.
     */
    public void increaseAmount() {
        // If the number of items in cart reached number of items in stock
        if (selectedPlant.reachMaxAmount()) {
            // Notify user that amount of items cannot be increased
            viewBean.displayObjectEditMessage("Not enough plants in stock");
            return;
        }
        
        // Otherwise - increase amount of items in cart
        controller.increaseAmount(this.selectedPlant);
        viewBean.displayObjectEditMessage("Changed Amount to " + this.selectedPlant.getAmount());
    }
    
    /**
     * Decrease the amount of a plant in the cart.
     */
    public void decreaseAmount() {
        controller.decreaseAmount(this.selectedPlant);
        viewBean.displayObjectEditMessage("Changed Amount to " + this.selectedPlant.getAmount());
    }
}
