/**
 * Edit plants bean.
 */
package PlantShop.beans;

import PlantShop.daos.PlantsDao;
import PlantShop.entities.Plant;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;

/**
 * Bean for edit_plants web page.
 * 
 * @author leagi
 */
@Named(value = "editPlantsBean")
@ViewScoped
public class EditPlantsBean implements Serializable{
    private ArrayList<Plant> plants;            // List of all plants in store
    private ArrayList<Plant> selectedPlants;    // Group of selected plants for deleting
    private Plant selectedPlant;                // Selected plant for editing
    private boolean newPlant;                   // Is the edited plant is a new plant
    
    @Inject
    private PlantsDao dao;
    
    /**
     * Gets all plants from DB using DAO.
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
     * Getter for selected plant for editing attribute.
     * 
     * @return the selected plant
     */
    public Plant getSelectedPlant() {
        return selectedPlant;
    }
    
    /**
     * Setter for selected plant for editing attribute.
     * 
     * @param selectedPlant 
     */
    public void setSelectedPlant(Plant selectedPlant) {
        this.selectedPlant = selectedPlant;
    }
    
    /**
     * Getter for selected plants for deleting attribute.
     * 
     * @return the selected plants
     */
    public ArrayList<Plant> getSelectedPlants() {
        return selectedPlants;
    }
    
    /**
     * Setter for selected plants for deleting attribute.
     * 
     * @param selectedPlants 
     */
    public void setSelectedPlants(ArrayList<Plant> selectedPlants) {
        this.selectedPlants = selectedPlants;
    }
    
    /**
     * Getter for isNewPlant attribute.
     * 
     * @return should a new plant be added
     */
    public boolean isNewPlant() {
        return newPlant;
    }
    
    /**
     * Setter for isNewPlant attribute.
     * @param newPlant 
     */
    public void setNewPlant(boolean newPlant) {
        this.newPlant = newPlant;
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
     */
    public void createPlant() {
        this.selectedPlant = new Plant();
        this.selectedPlant.setId(getAvailablePlantId());
    }
    
    /**
     * Remove selected plant from application.
     */
    public void removeSelectedPlant() {
        this.plants.remove(this.selectedPlant);
        dao.removePlant(this.selectedPlant);
        this.selectedPlant = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Plant Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-plants");
    }
    
    /**
     * Remove group of selected plants from application.
     */
    public void removeSelectedPlants() {
        this.plants.removeAll(this.selectedPlants);
        dao.removePlants(this.selectedPlants);
        this.selectedPlants = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Plant Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-plants");
        PrimeFaces.current().executeScript("PF('dtPlants').clearFilters()");
    }
    
    /**
     * If any plant is in the selected plants group.
     * 
     * @return if there is any plant in the group
     */
    public boolean hasSelectedPlants() {
        return this.selectedPlants != null && !this.selectedPlants.isEmpty();
    }
    
    /**
     * Returns the message displayed when removing plants from application.
     * 
     * @return the message to display
     */
    public String getDeleteButtonMessage() {
        if (hasSelectedPlants()) {
            int size = this.selectedPlants.size();
            return size > 1 ? size + " plants selected" : "1 plant selected";
        }

        return "Delete";
    }
    
    /**
     * Uploads image to the application.
     * 
     * @param event
     */
    public void handleImageUpload(FileUploadEvent event) {
        String fileName = event.getFile().getFileName();
        try {
            String path = "C:\\Users\\leagi\\OneDrive\\Documents\\NetBeansProjects\\PlantShop\\PlantShopMaven\\src\\main\\webapp\\images";
            InputStream input = event.getFile().getInputStream();
            System.out.println(path);
            OutputStream out = new FileOutputStream(new File(path + "\\" + fileName));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = input.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            input.close();
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Save the state of the edited plant.
     */
    public void save() {
        // Add plant to application if it's new
        if (this.newPlant) {
            dao.addPlant(selectedPlant);
            this.newPlant = false;
            this.plants.add(selectedPlant);
        }
        // Otherwise - update the existing plant
        else {
            dao.updatePlant(selectedPlant);
        }
    }
}
