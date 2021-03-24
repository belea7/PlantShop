package PlantShop.view;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 * Bean for managing view elements in the registration page.
 * @author Ron Mosenzon
 */
@Named(value = "registrationViewBean")
@Dependent
public class RegistrationViewBean extends AbstractFormViewBean {

    @Override
    public void displayFormSubmissionErrorMessage(String msg) {
        super.displayFormSubmissionErrorMessage(msg);
        PrimeFaces.current().ajax().update("form:messages");
    }
}
