package PlantShop.view;

import PlantShop.model.UserModel;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 * A bean for managing view elements in the profile page.
 * @author Ron Mosenzon
 */
@Named(value = "profileViewBean")
@ViewScoped
public class ProfileViewBean implements Serializable {
    
    @Inject
    private UserModel userBean;
    
    /**
     * @return The first name of the user that is currently logged in,
     * or an empty string if no user is currently logged in.
     */
    public String getFirstName() {
        if( !userBean.isLoggedIn() )
            return "";
        return String.valueOf(userBean.getUser().getFirstName());
    }
    
    /**
     * @return The last name of the user that is currently logged in,
     * or an empty string if no user is currently logged in.
     */
    public String getLastName() {
        if( !userBean.isLoggedIn() )
            return "";
        return String.valueOf(userBean.getUser().getLastName());
    }
    
    /**
     * @return The phone number of the user that is currently logged in,
     * or an empty string if no user is currently logged in.
     */
    public String getPhoneNumber() {
        if( !userBean.isLoggedIn() )
            return "";
        return String.valueOf(userBean.getUser().getPhoneNumber());
    }
    
    /**
     * @return The user name of the user that is currently logged in,
     * or an empty string if no user is currently logged in.
     */
    public String getUsername() {
        if( !userBean.isLoggedIn() )
            return "";
        return String.valueOf(userBean.getUser().getUsername());
    }
    
    /**
     * @return The email address of the user that is currently logged in,
     * or an empty string if no user is currently logged in.
     */
    public String getEmailAddress() {
        if( !userBean.isLoggedIn() )
            return "";
        return String.valueOf(userBean.getUser().getEmailAddress());
    }
    
    /**
     * @return The city of the user that is currently logged in,
     * or an empty string if no user is currently logged in.
     */
    public String getCity() {
        if( !userBean.isLoggedIn() )
            return "";
        return String.valueOf(userBean.getUser().getCity());
    }
    
    /**
     * @return The street of the user that is currently logged in,
     * or an empty string if no user is currently logged in.
     */
    public String getStreet() {
        if( !userBean.isLoggedIn() )
            return "";
        return String.valueOf(userBean.getUser().getStreet());
    }
    
    /**
     * @return The house number of the user that is currently logged in,
     * or an empty string if no user is currently logged in.
     */
    public String getHouseNumber() {
        if( !userBean.isLoggedIn() )
            return "";
        return String.valueOf(userBean.getUser().getHouseNumber());
    }
    
    /**
     * @return The apartment of the user that is currently logged in,
     * or an empty string if no user is currently logged in.
     */
    public String getAppartment() {
        if( !userBean.isLoggedIn() )
            return "";
        return String.valueOf(userBean.getUser().getAppartment());
    }
    
    /**
     * @return The zip code of the user that is currently logged in,
     * or an empty string if no user is currently logged in.
     */
    public String getZipcode() {
        if( !userBean.isLoggedIn() )
            return "";
        return String.valueOf(userBean.getUser().getZipcode());
    }
    
    /**
     * @return The birth date of the user that is currently logged in,
     * or an empty string if no user is currently logged in.
     */
    public String getBirthDate() {
        if( !userBean.isLoggedIn() )
            return "";
        return String.valueOf(userBean.getUser().getBirthDate());
    }
    
}
