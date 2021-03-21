/**
 * Plants controller bean
 */
package PlantShop.controllers;

import PlantShop.daos.PlantsDao;
import PlantShop.entities.Plant;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Controller bean for managing plants in the store.
 * 
 * @author leagi
 */
@Named(value = "plantsController")
@ViewScoped
public class PlantsController implements Serializable{
    
    private ArrayList<Plant> plants;        // List of all plants in store
    
    @Inject
    private PlantsDao dao;                  // Plants DAO
    
    /**
     * Gets all plants in shop from DAO.
     */
    @PostConstruct
    public void init() {
        plants = dao.getPlants();
    }
    
    /**
     * Getter for plants attribute.
     * 
     * @return list of plants
     */
    public ArrayList<Plant> getPlants() {
        return this.plants;
    }
    
    /**
     * Finds an available ID for a new plant.
     * Checks if there's any available ID that is less than the largest taken
     * ID, otherwise returns the next index after it.
     * 
     * For example: If there's 19 plants but there's an available ID 16 - this
     * is the new ID. If no available ID less than 19 exists - returns 20.
     * 
     * @return an available ID
     */
    public int getAvailablePlantId() {
        // Get the largest taken ID
        int size = this.plants.size();
        int lastId = this.plants.get(size - 1).getId();
        
        // If the last ID is not equal to the number of plants - 
        // an available ID less than the largest ID exists.
        if (lastId != size) {
            //Create a list of all taken ID's
            ArrayList<Integer> indexes = new ArrayList();
            for (Plant p: plants)
                indexes.add(p.getId());
            
            // Return an available ID
            for (int i=1; i < lastId; i++) {
                if (!indexes.contains(i))
                    return i;
            }
        }
        // If the last ID is equal to the number of plants -
        // return the next available ID
        return size + 1;
    }
    
    /**
     * Create an instance of the new plant to be added and mark as selected
     * plant to be edited.
     * 
     * @return the created instance
     */
    public Plant createPlant() {
        Plant plant = new Plant();
        plant.setId(getAvailablePlantId());
        return plant;
    }
    
    /**
     * Remove plant from application.
     * 
     * @param plant
     */
    public void removePlant(Plant plant) {
        this.plants.remove(plant);
        dao.removePlant(plant);
    }
    
    /**
     * Add plant to application.
     * 
     * @param plant 
     */
    public void addPlant(Plant plant) {
        dao.addPlant(plant);
        this.plants.add(plant);
    }
    
    /**
     * Update a plant in the shop.
     * 
     * @param plant 
     */
    public void updatePlant(Plant plant) {
        dao.updatePlant(plant);
    }
}
