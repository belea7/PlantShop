package PlantShop.controller;

import PlantShop.entities.User;
import PlantShop.exceptions.DaoException;
import PlantShop.exceptions.ProfileInputFormatException;
import PlantShop.exceptions.UsernameTakenException;
import PlantShop.model.UserModel;
import PlantShop.view.MessagesView;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 * Bean that gets the user's information from registration
 * page and puts it into the database.
 * @author Ron Mosenzon
 */
@Named(value = "registrationController")
@ViewScoped
public class RegistrationController extends AbstractProfileDataInputController {
    
    private final String ADMIN_CODE = "admin";
    
    
    private String admin = ""; // for "admin" input field in registration page
    
    @Inject
    private MessagesView messagesView;
    
    @Inject
    private UserModel userModel;
    
    
    public RegistrationController() {
        
    }
    
    
    /**
     * Method called when submit button is pressed in registration page.
     * If the user's information is valid, add it to the database and
     * navigate to "registered" page. Otherwise, stay on registration page by
     * returning the string "null".
     * @return The page to navigate to. Either "null" or "registered" depending
     * on whether the registration was successful.
     */
    public String register(){
        
        User.ProfileDetails profile; // user object for storing parsed registration info
        boolean isAdmin = (admin.equals(ADMIN_CODE));
        
        // parse inputs
        try{
            profile = parseInputDetails(messagesView);
        } catch(ProfileInputFormatException e){
            return null;
        }
        
        
        // register new user
        try{
            userModel.register(getInputUserName(), profile, isAdmin);
        } catch(DaoException e) {
            messagesView.displayErrorMessage(
                    "Oops! Something went wrong when connecting to the database.");
            return null;
        } catch(UsernameTakenException e) {
            messagesView.displayErrorMessage(
                    "That username is already taken!");
            return null;
        }
        
        // return navigation result
        return "index";
    }
    
    
    /**
     * used by InputText component in registration page.
     * @return 
     */
    public String getAdmin() {
        return admin;
    }
    
    /**
     * Received input from InputText component in registration page.
     * @param admin 
     */
    public void setAdmin(String admin) {
        this.admin = admin;
    }
    
}