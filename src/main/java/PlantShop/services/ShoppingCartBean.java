/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlantShop.services;

import PlantShop.daos.ShoppingCartDao;
import PlantShop.entities.Plant;
import PlantShop.entities.PlantInCart;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author leagi
 */
@ManagedBean
@Named(value = "shoppingCartBean")
@ViewScoped
public class ShoppingCartBean implements Serializable{
    private ArrayList<PlantInCart> plants;
    private ArrayList<PlantInCart> selectedPlants;
    private PlantInCart selectedPlant;
    private Plant addedPlant;
    
    @Inject
    private ShoppingCartDao dao;
    
    @PostConstruct
    public void init() {
        plants = dao.getPlants();
    }
    
    public ArrayList<PlantInCart> getPlants() {
        return this.plants;
    }

    public PlantInCart getSelectedPlant() {
        return selectedPlant;
    }

    public void setSelectedPlant(PlantInCart selectedPlant) {
        this.selectedPlant = selectedPlant;
    }

    public Plant getAddedPlant() {
        return addedPlant;
    }

    public void setAddedPlant(Plant addedPlant) {
        this.addedPlant = addedPlant;
    }

    public ArrayList<PlantInCart> getSelectedPlants() {
        return selectedPlants;
    }

    public void setSelectedPlants(ArrayList<PlantInCart> selectedPlants) {
        this.selectedPlants = selectedPlants;
    }
    
    public void addToCart() {
        dao.addPlantToCart(addedPlant);
    }
    
    public void removeSelectedPlants() {
        dao.removePlantsFromCart(this.selectedPlants);
        this.plants.removeAll(this.selectedPlants);
        this.selectedPlants = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Plant Removed From Cart"));
        PrimeFaces.current().ajax().update("form:messages", "form:sc-plants");
        PrimeFaces.current().executeScript("PF('scPlants').clearFilters()");
    }
    
    public boolean hasSelectedPlants() {
        return this.selectedPlants != null && !this.selectedPlants.isEmpty();
    }
    
    public void removeFromCart() {
        plants.remove(selectedPlant);
        dao.removePlantFromCart(selectedPlant);
        selectedPlant = null;
    }
    
    public String getDeleteButtonMessage() {
        if (hasSelectedPlants()) {
            int size = this.selectedPlants.size();
            return size > 1 ? size + " plants selected" : "1 plant selected";
        }

        return "Delete";
    }
    
    public void saveAmount(PlantInCart plant) {
        dao.saveAmount(plant);
    }
}
