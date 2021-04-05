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
    
    /**
     * Getter for the username property, used by the inputText component in the login page.
     * @return 
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Setter for the username property, used by the inputText component in the login page.
     * @param username the username that the user entered in the inputComponent.
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Getter for the password property, used by the inputText component in the login page.
     * @return 
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Setter for the password property, used by the inputText component in the login page.
     * @param password the password that the user entered in the inputComponent.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
}