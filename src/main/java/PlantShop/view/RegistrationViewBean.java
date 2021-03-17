package PlantShop.view;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 * Bean for managing view elements in the registration page.
 * @author Ron Mosenzon
 */
@Named(value = "registrationViewBean")
@Dependent
public class RegistrationViewBean extends AbstractFormViewBean {

    /**
     * Creates a new instance of RegistrationViewBean
     */
    public RegistrationViewBean() {
    }
    
}

