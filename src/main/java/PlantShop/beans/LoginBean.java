package PlantShop.beans;

import PlantShop.exceptions.DaoException;
import PlantShop.exceptions.IncorrectCredentialsException;
import PlantShop.view.LoginViewBean;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


/**
 * Controller bean that gets the user's input from the login page and tells
 * passes it on to the model.
 * @author Ron Mosenzon
 */
@Named(value = "loginBean")
@ViewScoped
public class LoginBean implements Serializable {
    
    private String username = "";
    private String password = "";
    
    @Inject
    private UserBean userBean;
    
    
    public LoginBean() {
    }
    
    
    /**
     * Method called when user tries to login.
     * Passes the information entered by the user to the model (UserBean) and
     * tells it to attempt to login.
     * @return either "null" or "index" depending on whether the login was
     * successful.
     */
    public String login() {
        
        try{
            userBean.login(username, password);
        } catch(DaoException e) {
            return null;
        } catch(IncorrectCredentialsException e) {
            return null;
        }
        
        return "index";
    }
    
    
    /**
     * Tells UserBean to logout.
     * @return page to navigate to after logout.
     */
    public String logout() {
        
        userBean.logOut();
        return "logged_out";
    }
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
