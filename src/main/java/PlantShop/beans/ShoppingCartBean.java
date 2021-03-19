/*
 * Shopping Cart bean.
 */
package PlantShop.beans;

import PlantShop.daos.ShoppingCartDao;
import PlantShop.entities.Plant;
import PlantShop.entities.PlantInCart;
import PlantShop.entities.ShoppingCart;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 * Bean for managing the shopping cart of the user.
 * 
 * @author leagi
 */
@Named(value = "shoppingCartBean")
@SessionScoped
public class ShoppingCartBean implements Serializable{
    
    private ShoppingCart cart;                          // The plants in the cart
    private ArrayList<PlantInCart> selectedPlants;      // The selected plants from cart for removal
    private PlantInCart selectedPlant;                  // The selected plant for editing
    private Plant addedPlant;                           // A plant that should be added to the cart
    
    @Inject
    private ShoppingCartDao dao;
    
    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }
    
    /**
     * Getter for plants in cart attribute.
     * 
     * @return plants in cart
     */
    public ShoppingCart getCart() {
        return this.cart;
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
     * Add selected plant to cart.
     */
    public void addToCart() {
        dao.addPlantToCart(addedPlant, cart);
    }
    
    /**
     * Remove group of selected plants from cart.
     */
    public void removeSelectedPlants() {
        dao.removePlantsFromCart(this.selectedPlants, cart);
        this.selectedPlants = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Plant Removed From Cart"));
        PrimeFaces.current().ajax().update("form:messages", "form:sc-plants");
        PrimeFaces.current().executeScript("PF('scPlants').clearFilters()");
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
     * Remove selected plant from cart.
     */
    public void removeFromCart() {
        dao.removePlantFromCart(selectedPlant, cart);
        selectedPlant = null;
    }
    
    /**
     * Returns the message being displayed before plants removal.
     * 
     * @return the message displayed
     */
    public String getDeleteButtonMessage() {
        if (hasSelectedPlants()) {
            int size = this.selectedPlants.size();
            return size > 1 ? size + " plants selected" : "1 plant selected";
        }
        return "Delete";
    }
    
    /**
     * Increase the amount of a plant in the cart.
     */
    public void increaseAmount() {
        this.selectedPlant.increaseAmount();
        dao.saveAmount(this.selectedPlant, cart);
    }
    
    /**
     * Decrease the amount of a plant in the cart.
     */
    public void decreaseAmount() {
        this.selectedPlant.decreaseAmount();
        dao.saveAmount(this.selectedPlant, cart);
    }
}
