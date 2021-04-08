/*
 * Edit plants Controller.
 */
package PlantShop.controller;

import PlantShop.model.PlantsModel;
import PlantShop.entities.Plant;
import PlantShop.exceptions.DaoException;
import PlantShop.exceptions.ImageDeletionException;
import PlantShop.view.MessagesView;

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
 * Controller for plants in the edit plants web page.
 * 
 * @author Lea Ben Zvi
 */
@Named(value = "editPlantsController")
@ViewScoped
public class EditPlantsController implements Serializable{
    
    private ArrayList<Plant> selectedPlants;    // Group of selected plants for deleting
    private Plant selectedPlant;                // Selected plant for editing
    private boolean newPlant;                   // Is the edited plant a new plant
    
    @Inject
    private PlantsModel plantsModel;
    
    @Inject
    private MessagesView messagesView;
    
    /**
     * Returns plants from controller.
     * 
     * @return list of plants
     */
    public ArrayList<Plant> getPlants() {
        try {
            plantsModel.updatePlants();
        } catch (DaoException e) {
            e.printStackTrace();
            messagesView.displayErrorMessage("Failed to update plants");
        }
        return plantsModel.getPlants();
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
        this.selectedPlant = plantsModel.createPlant();
    }
    
    /**
     * Remove selected plant from application.
     */
    public void removeSelectedPlant() {
        try {
            plantsModel.removePlant(selectedPlant);
        } catch (DaoException e) {
            e.printStackTrace();
            messagesView.displayErrorMessage("Failed to remove plant");
            return;
        } catch (ImageDeletionException e) {
            e.printStackTrace();
            messagesView.displayErrorMessage("Failed to remove picture, please remove it manually");
        }
        this.selectedPlant = null;
        PrimeFaces.current().ajax().update("form:dt-plants");
        messagesView.displayInfoMessage("Plant was removed");
    }
    
    /**
     * Remove group of selected plants from application.
     */
    public void removeSelectedPlants() {
        try {
            for (Plant plant: this.selectedPlants)
                plantsModel.removePlant(plant);
        } catch (DaoException e) {
            e.printStackTrace();
            messagesView.displayErrorMessage("Failed to remove plants");
            return;
        } catch (ImageDeletionException e) {
            e.printStackTrace();
            messagesView.displayErrorMessage("Failed to remove a picture, please remove it manually");
        }
        PrimeFaces.current().ajax().update("form:dt-plants");
        messagesView.displayInfoMessage(selectedPlants.size() + " items were removed");
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
                plantsModel.addPlant(selectedPlant);
            } catch (DaoException e) {
                e.printStackTrace();
                messagesView.displayErrorMessage("Failed to save plant");
                return;
            }
            this.newPlant = false;
        }
        // Otherwise - update the existing plant
        else {
            try {
                plantsModel.updatePlant(selectedPlant);
            } catch (DaoException e) {
                e.printStackTrace();
                messagesView.displayErrorMessage("Failed to save plant");
                return;
            }
        }
        messagesView.displayInfoMessage(selectedPlant.getName() + " was created");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-plants", "manage-plant-content");
        PrimeFaces.current().executeScript("PF('managePlantDialog').hide()");
    }
    
    /**
     * Uploads image to the application.
     * After user uploads the picture, it is saved in an "images" directory
     * on the web server (the directory is created if needed).
     * If a file with the same name exists in directory already - the operation is aborted.
     * 
     * @param event
     */
    public void handleImageUpload(FileUploadEvent event) {
        String fileName = event.getFile().getFileName();
        String path = System.getProperty("user.dir") + "\\images\\";
        File file = new File(path + fileName);
        
        try {
            // If images directory on web server doesn't exist - create it
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            
            // If a file with this name exists in the directory - ask for an other image
            if (file.exists()) {
                String msg = " already exists. Please upload a different file";
                messagesView.displayErrorMessage(fileName + msg);
                return;
            }
            
            // Copy uploaded image to new directory
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
            messagesView.displayInfoMessage("'" + fileName + "' was successfully uploaded");
            PrimeFaces.current().ajax().update("form:messages", "form:manage-plant-content");
        }
        catch (IOException e) {
            e.printStackTrace();
            messagesView.displayErrorMessage("Failed to upload the image to server");
        }

    }
}