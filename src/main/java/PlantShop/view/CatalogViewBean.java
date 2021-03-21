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
@Named(value = "catalogViewBean")
@Dependent
public class CatalogViewBean extends AbstractFormViewBean{

    /**
     * Creates a new instance of CatalogViewBean
     */
    public CatalogViewBean() {
    }
    
    public void displayInforamtionMessage(String msg) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().update("form:messages", "form:dt-plants");
    }
    
}
