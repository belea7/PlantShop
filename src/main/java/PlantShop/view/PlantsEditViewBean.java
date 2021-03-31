/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlantShop.view;

import javax.inject.Named;
import javax.enterprise.context.Dependent;
import org.primefaces.PrimeFaces;

/**
 *
 * @author leagi
 */
@Named(value = "plantsEditViewBean")
@Dependent
public class PlantsEditViewBean extends AbstractFormViewBean{
    
    @Override
    public void displayFormSubmissionInfoMessage(String msg) {
        super.displayFormSubmissionInfoMessage(msg);
        PrimeFaces.current().ajax().update("form:messages", "form:dt-plants");
        PrimeFaces.current().executeScript("PF('dtPlants').clearFilters()");
    }
    
    @Override
    public void displayFormSubmissionErrorMessage(String msg) {
        super.displayFormSubmissionErrorMessage(msg);
        PrimeFaces.current().ajax().update("form:messages", "form:dt-plants", "manage-plant-content");
        PrimeFaces.current().executeScript("PF('dtPlants').clearFilters()");
    }
}