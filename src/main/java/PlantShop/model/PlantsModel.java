/**
 * Plants model bean
 */
package PlantShop.model;

import PlantShop.daos.PlantsDao;
import PlantShop.entities.Plant;
import PlantShop.exceptions.DaoException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 * Model bean for managing plants in the store.
 * 
 * @author leagi
 */
@Named(value = "plantsModel")
@ViewScoped
public class PlantsModel implements Serializable{
    
    private ArrayList<Plant> plants;        // List of all plants in store
    
    @Inject
    private PlantsDao dao;                  // Plants DAO
    
    /**
     * Gets all plants in shop from DAO.
     * 
     */
    @PostConstruct
    public void init(){
        try {
            plants = dao.getPlants();
        } catch (DaoException e) {
            e.printStackTrace();
        }
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
    public long getAvailablePlantId() {
        if (this.plants.isEmpty())
            return 1;
        
        // Get the largest taken ID
        long size = this.plants.size();
        long lastId = Collections.max(this.plants).getId();
        
        // If the last ID is not equal to the number of plants - 
        // an available ID less than the largest ID exists.
        if (lastId != size) {
            //Create a list of all taken ID's
            ArrayList<Long> indexes = new ArrayList();
            for (Plant p: plants)
                indexes.add(p.getId());
            
            // Return an available ID
            for (long i=1; i < lastId; i++) {
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
        System.out.println("creating");
        Plant plant = new Plant();
        plant.setId(getAvailablePlantId());
        return plant;
    }
    
    /**
     * Remove plant from application.
     * 
     * @param plant
     * @throws DaoException
     */
    public void removePlant(Plant plant) throws DaoException{
        this.plants.remove(plant);
        dao.removePlant(plant);
    }
    
    /**
     * Add plant to application.
     * 
     * @param plant
     * @throws DaoException
     */
    public void addPlant(Plant plant) throws DaoException{
        dao.addPlant(plant);
        this.plants.add(plant);
    }
    
    /**
     * Update a plant in the shop.
     * 
     * @param plant 
     * @throws DaoException
     */
    public void updatePlant(Plant plant) throws DaoException{
        dao.updatePlant(plant);
    }
}