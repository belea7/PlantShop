/*
 * Shopping Cart Controller
 */
package PlantShop.controllers;

import PlantShop.daos.ShoppingCartDao;
import PlantShop.entities.Plant;
import PlantShop.entities.PlantInCart;
import PlantShop.entities.ShoppingCart;
import PlantShop.exceptions.DaoException;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Controller bean for managing the shopping cart.
 * 
 * @author leagi
 */
@Named(value = "shoppingCartController")
@SessionScoped
public class ShoppingCartController implements Serializable{
    
    private ShoppingCart cart;          // Cart entity
    
    @Inject
    private ShoppingCartDao dao;        // Shopping Cart DAO
    
    /**
     * Setter for shopping cart attribute.
     * 
     * @param cart 
     */
    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }
    
    /**
     * Getter for shopping cart attribute.
     * 
     * @return plants in cart
     */
    public ShoppingCart getCart() {
        return this.cart;
    }
    
    /**
     * Add plant to cart.
     * 
     * @param plant
     * @throws DaoException
     */
    public void addToCart(Plant plant) throws DaoException{
        PlantInCart plantInCart = new PlantInCart(plant, 1);
        cart.addToCart(plantInCart);
        dao.addPlantToCart(plantInCart, cart);
    }
    
    /**
     * Remove plant from cart.
     * 
     * @param plant
     * @throws DaoException
     */
    public void removeFromCart(PlantInCart plant) throws DaoException{
        cart.removeFromCart(plant);
        dao.removePlantFromCart(plant, cart);
    }
    
    /**
     * Increase the amount of a plant in the cart.
     * 
     * @param plant
     * @throws DaoException
     */
    public void increaseAmount(PlantInCart plant) throws DaoException {
        plant.increaseAmount();
        dao.saveAmount(plant, cart);
    }
    
    /**
     * Decrease the amount of a plant in the cart.
     * 
     * @param plant
     * @throws DaoException
     */
    public void decreaseAmount(PlantInCart plant) throws DaoException {
        plant.decreaseAmount();
        dao.saveAmount(plant, cart);
    }
}
