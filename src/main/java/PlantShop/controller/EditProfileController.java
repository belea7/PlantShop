package PlantShop.controller;

import PlantShop.entities.ShoppingCart;
import PlantShop.entities.User;
import PlantShop.exceptions.DaoException;
import PlantShop.exceptions.ProfileInputFormatException;
import PlantShop.exceptions.UsernameTakenException;
import PlantShop.model.UserModel;
import PlantShop.util.ErrorDisplay;
import PlantShop.view.MessagesView;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 * Controller bean for gathering the user's input in the edit profile
 * page and passing them on to the model.
 * @author Ron Mosenzon
 */
@Named(value = "editProfileBean")
@ViewScoped
public class EditProfileController extends AbstractProfileDataInputController {
    
    @Inject
    private MessagesView messagesView;
    
    @Inject
    private UserModel userBean;
    
    /**
     * Creates a new instance of EditProfileBean
     */
    public EditProfileController() {
    }
    
    
    @Override
    protected User.ProfileDetails parseInputDetails(ErrorDisplay errorDisplay)
            throws ProfileInputFormatException {
        User.ProfileDetails profile = super.parseInputDetails(errorDisplay);
        profile.setPassword(userBean.getUser().getPassword());
        return profile;
    }
    
    
    /**
     * Method called when Edit Profile button is pressed in edit profile page.
     * If the submitted information is valid, perform the profile edit.
     * @return the page to navigate to. Either "null" or "profile" depending
     * on whether the edit was successful.
     */
    public String editProfile() {
        
        User.ProfileDetails profile; // for storing the received user details.
        
        try{
            profile = this.parseInputDetails(messagesView);
        } catch(ProfileInputFormatException e) {
            return null;
        }
        
        try{
            userBean.editProfile(getInputUserName(), profile);
        } catch(DaoException e) {
            messagesView.displayErrorMessage(
                    "Oops! Something went wrong when connecting to the database.");
            return null;
        } catch(UsernameTakenException e) {
            messagesView.displayErrorMessage(
                    "That username is already taken!");
            return null;
        }
        return "profile";
        
    }
    
}
