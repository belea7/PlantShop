package PlantShop.controller;

import PlantShop.entities.User;
import PlantShop.exceptions.DaoException;
import PlantShop.exceptions.ProfileInputFormatException;
import PlantShop.exceptions.UsernameTakenException;
import PlantShop.model.UserModel;
import PlantShop.view.MessagesView;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 * Controller bean for gathering the user's input in the edit profile
 * page and passing them on to the model.
 * @author Ron Mosenzon
 */
@Named(value = "editProfileController")
@ViewScoped
public class EditProfileController extends AbstractProfileDataInputController {
    
    @Inject
    private MessagesView messagesView;
    
    @Inject
    private UserModel userModel;
    
    /**
     * Creates a new instance of EditProfileBean
     */
    public EditProfileController() {
    }
    
    
    /**
     * Initializes the editable profile details to the user's current details.
     */
    @PostConstruct
    public void initProfileDetails() {
        
        if( !userModel.isLoggedIn() ) {
            System.err.println("Loaded edit_profile page when not logged in.");
            return;
        }
        
        this.setUsername(userModel.getUser().getUsername());
        this.setFirstName(userModel.getUser().getFirstName());
        this.setLastName(userModel.getUser().getLastName());
        this.setBirthdayDate(String.valueOf(userModel.getUser().getBirthdayDate()));
        this.setEmailAddress(userModel.getUser().getEmailAddress());
        this.setPhoneNumber(userModel.getUser().getPhoneNumber());
        this.setCity(userModel.getUser().getCity());
        this.setStreet(userModel.getUser().getStreet());
        this.setHouseNumber(String.valueOf(userModel.getUser().getHouseNumber()));
        this.setAppartment(String.valueOf(userModel.getUser().getAppartment()));
        this.setZipcode(String.valueOf(userModel.getUser().getZipcode()));
        this.setPassword(userModel.getUser().getPassword());
    }
    
    
    /**
     * Method called when Edit Profile button is pressed in edit profile page.
     * If the submitted information is valid, perform the profile edit.
     * @return the page to navigate to. Either "null" or "profile" depending
     * on whether the edit was successful.
     */
    public String editProfile() {
        
        if( !userModel.isLoggedIn() ) {
            messagesView.displayErrorMessage(
                    "Cannot edit profile because not logged in as any user.");
            return null;
        }
        
        User.ProfileDetails profile; // for storing the received user details.
        
        try{
            profile = this.parseInputDetails(messagesView);
        } catch(ProfileInputFormatException e) {
            return null;
        }
        
        try{
            userModel.editProfile(getInputUserName(), profile);
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
