/*
 * Shopping cart controller.
 */
package PlantShop.controller;

import PlantShop.daos.OrdersDao;
import PlantShop.entities.Order;
import PlantShop.entities.Plant;
import PlantShop.entities.PlantInCart;
import PlantShop.exceptions.DaoException;
import PlantShop.model.ShoppingCartModel;
import PlantShop.view.MessagesView;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 * Controller for managing shopping cart page.
 * 
 * @author leagi
 */
@Named(value = "shoppingCartController")
@ViewScoped
public class ShoppingCartController implements Serializable{
    
    private ArrayList<PlantInCart> selectedPlants;      // The selected plants from cart for removal
    private PlantInCart selectedPlant;                  // The selected plant for editing
    private Plant addedPlant;                           // A plant that should be added to the cart
    
    @Inject
    private ShoppingCartModel model;
    
    @Inject
    private MessagesView messagesView;
    
    @Inject
    private OrdersDao ordersDao;

    /**
     * Returns plants in cart from shopping cart controller.
     * 
     * @return list of plants
     */
    public ArrayList<PlantInCart> getPlants() {
        return model.getCart().getPlantsInCart();
    }
    
    /**
     * Returns total cart's price from shopping cart controller.
     * 
     * @return total price
     */
    public double getTotalPrice() {
        return model.getCart().getTotalPrice();
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
        try {
            model.removeFromCart(selectedPlant);
        } catch (DaoException e) {
            e.printStackTrace();
            messagesView.displayErrorMessage("Failed to remove plant from cart");
            return;
        }
        String name = this.selectedPlant.getPlant().getName();
        messagesView.displayInfoMessage(name + "was removed from cart");
        this.selectedPlant = null;
    }
    
    /**
     * Remove group of selected plants from cart and notify user.
     */
    public void removeSelectedPlants() {
        for (PlantInCart plant: this.selectedPlants) {
            try {
                model.removeFromCart(plant);
            } catch (DaoException e) {
                e.printStackTrace();
                messagesView.displayErrorMessage("Failed to remove plants");
                return;
            }
        }
        messagesView.displayInfoMessage("Plants removed from cart");
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
            messagesView.displayErrorMessage("Not enough plants in stock");
            return;
        }
        
        // Otherwise - increase amount of items in cart
        try {
            model.increaseAmount(this.selectedPlant);
        } catch (DaoException e) {
                e.printStackTrace();
                messagesView.displayErrorMessage("Failed to Increase amount");
                return;
        }
        messagesView.displayInfoMessage("Changed Amount to " + this.selectedPlant.getAmount());
    }
    
    /**
     * Decrease the amount of a plant in the cart.
     */
    public void decreaseAmount() {
        try {
            model.decreaseAmount(this.selectedPlant);
        } catch (DaoException e) {
                e.printStackTrace();
                messagesView.displayErrorMessage("Failed to Decrease amount");
                return;
        }
        messagesView.displayInfoMessage("Changed Amount to " + this.selectedPlant.getAmount());
    }
    
    /**
     * Order the items in the user's cart and then empty the cart.
     */
    public void checkOut() {
        
        // build list of PlantInOrder objects
        ArrayList<Order.PlantInOrder> plantsInOrder = new ArrayList<>();
        for (PlantInCart plant: model.getCart().getPlantsInCart()) {
            plantsInOrder.add(new Order.PlantInOrder(plant.getPlant(), plant.getAmount()));
        }
        
        // make order
        try{
            ordersDao.AddNewOrder(model.getCart().getUser().getUsername()
                    , Timestamp.from(Instant.now()), "Not sent yet", plantsInOrder);
        } catch(DaoException e) {
            e.printStackTrace();
            messagesView.displayErrorMessage("Failed to make order");
        }
        
        // empty the cart
        while( !model.getCart().getPlantsInCart().isEmpty() ) {
            PlantInCart nextPlant = model.getCart().getPlantsInCart().get(0);
            try {
                model.removeFromCart(nextPlant);
            } catch (DaoException e) {
                e.printStackTrace();
                messagesView.displayErrorMessage("Failed to remove plant from cart");
                return;
            }
        }
    }
}
