package PlantShop.view;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 * Bean for managing view elements in the login page.
 * @author Ron Mosenzon
 */
@Named(value = "loginViewBean")
@Dependent
public class LoginViewBean extends AbstractFormViewBean {

    @Override
    public void displayFormSubmissionErrorMessage(String msg) {
        super.displayFormSubmissionErrorMessage(msg);
        PrimeFaces.current().ajax().update("form:messages");
    }
}