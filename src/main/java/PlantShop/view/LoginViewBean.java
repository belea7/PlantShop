package PlantShop.view;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 * Bean for managing view elements in the login page.
 * @author Ron Mosenzon
 */
@Named(value = "loginViewBean")
@Dependent
public class LoginViewBean extends AbstractFormViewBean {

    /**
     * Creates a new instance of LoginViewBean
     */
    public LoginViewBean() {
    }
    
}
