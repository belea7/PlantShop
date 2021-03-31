/*
 * Edit plants bean.
 */
package PlantShop.controller;

import PlantShop.model.PlantsController;
import PlantShop.entities.Plant;
import PlantShop.exceptions.DaoException;
import PlantShop.view.PlantsEditViewBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
        System.out.println(this.selectedPlant);
    }
    
    /**
     * Remove selected plant from application.
     */
    public void removeSelectedPlant() {
        try {
            controller.removePlant(selectedPlant);
        } catch (DaoException e) {
            e.printStackTrace();
            viewBean.displayFormSubmissionErrorMessage("Failed to remove plant");
        }
        this.selectedPlant = null;
        viewBean.displayFormSubmissionInfoMessage("Plant removed");
    }
    
    /**
     * Remove group of selected plants from application.
     */
    public void removeSelectedPlants() {
        try {
            for (Plant plant: this.selectedPlants)
                controller.removePlant(plant);
        } catch (DaoException e) {
            e.printStackTrace();
            viewBean.displayFormSubmissionErrorMessage("Failed to remove plants");
        }
        viewBean.displayFormSubmissionInfoMessage(selectedPlants.size() + " items were removed");
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
            try {
                controller.addPlant(selectedPlant);
            } catch (DaoException e) {
                e.printStackTrace();
                viewBean.displayFormSubmissionErrorMessage("Failed save plant");
            }
            this.newPlant = false;
        }
        // Otherwise - update the existing plant
        else {
            try {
                controller.updatePlant(selectedPlant);
            } catch (DaoException e) {
                e.printStackTrace();
                viewBean.displayFormSubmissionErrorMessage("Failed save plant");
            }
        }
        viewBean.displayFormSubmissionInfoMessage(selectedPlant.getName() + " was created");
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
        String path = System.getProperty("user.dir") + "\\images\\";
        File file = new File(path + fileName);
        try {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            if (file.exists()) {
                String msg = " already exists. Please upload different file";
                viewBean.displayFormSubmissionErrorMessage(fileName + msg);
                return;
            }
            InputStream input = event.getFile().getInputStream();
            OutputStream out = new FileOutputStream(file);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = input.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            input.close();
            out.flush();
            out.close();
            selectedPlant.setPicture(file.getAbsolutePath());
            viewBean.displayFormSubmissionInfoMessage("'" + fileName + "' was successfully uploaded");
            PrimeFaces.current().ajax().update("form:messages", "form:manage-plant-content");
        }
        catch (IOException e) {
            e.printStackTrace();
            viewBean.displayFormSubmissionErrorMessage("Failed to upload the image to server");
        }

    }
}
