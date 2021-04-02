package PlantShop.controller;

import PlantShop.exceptions.DaoException;
import PlantShop.exceptions.IncorrectCredentialsException;
import PlantShop.model.UserModel;
import PlantShop.view.MessagesView;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


/**
 * Controller bean that gets the user's input from the login page and tells
 * passes it on to the model.
 * @author Ron Mosenzon
 */
@Named(value = "loginController")
@ViewScoped
public class LoginController implements Serializable {
    
    private String username = "";
    private String password = "";
    
    @Inject
    private MessagesView messagesView;
    
    @Inject
    private UserModel userModel;
    
    
    public LoginController() {
    }
    
    
    /**
     * Method called when user tries to login.
     * Passes the information entered by the user to the model (UserBean) and
     * tells it to attempt to login.
     * @return either "null" or "index" depending on whether the login was
     * successful.
     */
    public String login() {
        
        if(userModel.isLoggedIn()) {
            messagesView.displayErrorMessage(
                    "Cannot log in because already logged in as " + userModel.getUser().getUsername());
            return null;
        }
        
        try{
            userModel.login(username, password);
        } catch(DaoException e) {
            messagesView.displayErrorMessage(
                    "Oops! Something went wrong when connecting to the database.");
            return null;
        } catch(IncorrectCredentialsException e) {
            messagesView.displayErrorMessage(
                    "Username or password is incorrect!");
            return null;
        }

        return "index";
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
