package PlantShop.view;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

/**
 * Template for beans that manage view elements in pages that have a form.
 * @author Ron Mosenzon
 */
public abstract class AbstractFormViewBean implements Serializable {
    
    /**
     * Displays an error message about the action that resulted from a form submission.
     * Must not be called from any thread other than the one upon which the servlet container
     * utilizes for the processing of the form submission request.
     */
    public void displayFormSubmissionErrorMessage(String msg) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().update("form:messages");
    }
    
}
