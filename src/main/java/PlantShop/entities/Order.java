/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlantShop.entities;

import PlantShop.exceptions.DaoException;
import PlantShop.exceptions.UninitiatedOrderContentsException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * a class representing an order.
 * @author Ron Mosenzon
 */
public class Order implements Serializable {
    
    private final long ID;
    private Timestamp timeOrdered;
    private String status; // either 'Not sent yet', 'On the way' or 'Arrived'
    private ArrayList<PlantInOrder> plants; // list of plants in the order, null if the list plants was no initiated yet
    
    
    /**
     * Constructs a new Order object.
     * @param ID the ID of this order in the database.
     * @param timeOrdered the time at which this order was ordered.
     * @param status the status of this order. either 'Not sent yet', 'On the way' or 'Arrived'.
     * @param plants the plants in this order.
     */
    public Order(long ID, Timestamp timeOrdered, String status, ArrayList<PlantInOrder> plants) {
        this.ID = ID;
        this.timeOrdered = timeOrdered;
        this.status = status;
        this.plants = new ArrayList<PlantInOrder>(plants);
    }
    
    
    /**
     * Constructs a new Order object, containing no plants.
     * The setPlants method of this Order will need to be called before
     * attempting to call getPlants or getPrice, otherwise these methods will
     * throw an UninitiatedOrderContentsException.
     * @param ID the ID of this order in the database.
     * @param timeOrdered the time at which this order was ordered.
     * @param status the status of this order. either 'Not sent yet', 'On the way' or 'Arrived'.
     */
    public Order(long ID, Timestamp timeOrdered, String status) {
        this.ID = ID;
        this.timeOrdered = timeOrdered;
        this.status = status;
        this.plants = null;
    }
    
    
    public Timestamp getTimeOrdered() {
        return this.timeOrdered;
    }
    
    
    /**
     * Sets the contents of this order.
     * @param plants the new contents of this order.
     */
    public void setPlants(ArrayList<PlantInOrder> plants) {
        this.plants = new ArrayList<PlantInOrder>(plants);
    }
    
    
    /**
     * @return the plants ordered in this order.
     * @throws UninitiatedOrderContentsException if this order was created using
     * the constructor that does not specify the plants in the order,
     * and setPlants was not called yet.
     */
    public ArrayList<PlantInOrder> getPlants() throws UninitiatedOrderContentsException {
        if(plants == null)
            throw new UninitiatedOrderContentsException("getPlants called before initiating plants list");
        return plants;
    }
    
    
    /**
     * @return the total price of this order. 
     * @throws UninitiatedOrderContentsException if this order was created using
     * the constructor that does not specify the plants in the order,
     * and setPlants was not called yet.
     */
    public double getPrice() throws UninitiatedOrderContentsException {
        if(plants == null)
            throw new UninitiatedOrderContentsException("getPrice called before initiating plants list");
        
        double sum = 0;
        
        for(PlantInOrder plant : plants)
           sum += plant.getTotalPrice();
        
        return sum;
    }
    
    
    /**
     * Set the status of this Order object.
     * @param status the new status.
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    
    /**
     * @return the current status of this order.
     */
    public String getStatus() {
        return status;
    }
    
    
    /**
     * @return The ID of this order in the database.
     */
    public long getID() {
        return ID;
    }
    
    
    /**
     * A class representing one type of plant from an order.
     */
    public static class PlantInOrder {
        
        private final Plant plant;
        private final int amount; // number of plants from this type in the order
        
        
        /**
         * Constructor.
         * @param plant the type of plant represented by this PlantInOrder object.
         * @param amount the amount of this plant that was ordered in the order.
         */
        public PlantInOrder(Plant plant, int amount) {
            this.plant = plant;
            this.amount = amount;
        }

        public Plant getPlant() {
            return plant;
        }

        public int getAmount() {
            return amount;
        }
        
        /**
         * @return The total price of all plants of this type in the order
         */
        public double getTotalPrice() {
            return plant.getPrice() * amount;
        }
        
    }
    
}
