/*
 * Edit plants bean.
 */
package PlantShop.models;

import PlantShop.controllers.PlantsController;
import PlantShop.entities.Plant;
import PlantShop.view.PlantsEditViewBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author leagi
 */
@Named(value = "editPlantsBean")
@ViewScoped
public class EditPlantsBean implements Serializable{
    
    private ArrayList<Plant> selectedPlants;    // Group of selected plants for deleting
    private Plant selectedPlant;                // Selected plant for editing
    private boolean newPlant;                   // Is the edited plant is a new plant
    
    @Inject
    private PlantsController controller;        // Plants controller
    
    @Inject
    private PlantsEditViewBean viewBean;        // Edit view bean
    
    /**
     * Returns plants from controller.
     * 
     * @return list of plants
     */
    public ArrayList<Plant> getPlants() {
        return controller.getPlants();
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
     * If any plant is in the selected plants group.
     * 
     * @return if there is any plant in the group
     */
    public boolean hasSelectedPlants() {
        return this.selectedPlants != null && !this.selectedPlants.isEmpty();
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
     * 
     * @param newPlant 
     */
    public void setNewPlant(boolean newPlant) {
        this.newPlant = newPlant;
    }
    
    /**
     * Create a new instance of plant for edit.
     */
    public void createPlant() {
        this.selectedPlant = controller.createPlant();
    }
    
    /**
     * Remove selected plant from application.
     */
    public void removeSelectedPlant() {
        controller.removePlant(selectedPlant);
        this.selectedPlant = null;
        viewBean.displayObjectDeletionMessage("Plant removed");
    }
    
    /**
     * Remove group of selected plants from application.
     */
    public void removeSelectedPlants() {
        for (Plant plant: this.selectedPlants)
            controller.removePlant(plant);
        viewBean.displayObjectDeletionMessage(selectedPlants.size() + " items were removed");
        this.selectedPlants = null;
    }
    
    /**
     * Returns the value of the remove button.
     * 
     * @return the message to display
     */
    public String getDeleteButtonMessage() {
        // If any plant is selected
        if (hasSelectedPlants()) {
            int size = this.selectedPlants.size();
            return size > 1 ? size + " plants selected" : "1 plant selected";
        }
        
        // If no plants selected
        return "Delete";
    }
    
    /**
     * Save the state of the plant being edited.
     */
    public void save() {
        // Add plant to application if it's new
        if (this.newPlant) {
            controller.addPlant(selectedPlant);
            this.newPlant = false;
        }
        // Otherwise - update the existing plant
        else {
            controller.updatePlant(selectedPlant);
        }
        viewBean.displayObjectCreationMessage(selectedPlant.getName() + " was created");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-plants", "manage-plant-content");
        PrimeFaces.current().executeScript("PF('managePlantDialog').hide()");
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
}
