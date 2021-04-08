/**
 * View for displaying messages to user
 */
package PlantShop.view;

import PlantShop.util.ErrorDisplay;

import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 * View bean for displaying messages to user.
 * 
 * @author Ron Mosenzon
 */
@Named("messagesView")
@Dependent
public class MessagesView implements ErrorDisplay, Serializable {
    
    /**
     * Display info message to the user.
     * 
     * @param msg the message to be displayed
     */
    public void displayInfoMessage(String msg) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().update("form:messages");
    }

    /**
     * Displays error message to user.
     * 
     * @param msg the message to be displayed
     */
    @Override
    public void displayErrorMessage(String msg) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().update("form:messages");
    }
}