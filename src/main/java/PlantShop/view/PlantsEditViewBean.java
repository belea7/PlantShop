/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlantShop.view;

import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

/**
 *
 * @author leagi
 */
@Named(value = "plantsEditViewBean")
@Dependent
public class PlantsEditViewBean extends AbstractDataEditorBean{
    @Override
    public void displayObjectDeletionMessage(String msg) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().update("form:messages", "form:dt-plants");
        PrimeFaces.current().executeScript("PF('dtPlants').clearFilters()");
    }
    
    @Override
    public void displayObjectEditMessage(String msg) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().update("form:messages", "form:dt-plants");
        PrimeFaces.current().executeScript("PF('dtPlants').clearFilters()");
    }
    
    @Override
    public void displayObjectCreationMessage(String msg) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().update("form:messages", "form:dt-plants");
        PrimeFaces.current().executeScript("PF('dtPlants').clearFilters()");
    }
    
    public void displayEditPlantError(String msg) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().update("form:messages", "form:dt-plants", "manage-plant-content");
        PrimeFaces.current().executeScript("PF('dtPlants').clearFilters()");
    }
}
