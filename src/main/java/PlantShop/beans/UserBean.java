package PlantShop.beans;

import PlantShop.daos.UserDao;
import PlantShop.entities.User;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.inject.Inject;

/**
 * This bean holds all of the user's session information.
 * @author Ron Mosenzon
 */
@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {
    
    /**
     * Information about the user that is currently logged in.
     * If no user is currently logged in, then this should be null.
     */
    private User user = null;
    
    @Inject
    private UserDao userDao;

    /**
     * Creates a new instance of UserBean.
     */
    public UserBean() {
    }
    
    
    /**
     * Attempts to log in to the account with the specified username and password.
     * @return true if login was successful, false otherwise.
     */
    public boolean login(String userName, String password) {
        
        User user = userDao.getUser(userName);
        if(user == null)
            return false; // user doesn't exist
        
        if(password.compareTo(user.getPassword()) != 0)
            return false; // wrong password
        
        this.user = user;
        return true;
    }
    
    
    /**
     * Attempts to register a new user with the specified information.
     * @return true if registration was successful, false otherwise.
     */
    public boolean register(User user) {
        
        if(userDao.getUser(user.getUsername()) != null)
            return false; // username already taken
        
        userDao.addUser(user);
        this.user = user;
        return true;
        
    }
    
    
    /**
     * Logs out the user.
     */
    public void logOut() {
        
        user = null;
        
    }

    
    public User getUser() {
        return user;
    }
    
    
    /**
     * @return true if the user is currently logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return (user != null);
    }
    
}
