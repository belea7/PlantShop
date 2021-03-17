package PlantShop.beans;

import PlantShop.daos.UserDao;
import PlantShop.entities.User;
import PlantShop.exceptions.DaoException;
import PlantShop.exceptions.IncorrectCredentialsException;
import PlantShop.exceptions.UsernameTakenException;
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
     * @throws DaoException if there was a problem with database access.
     * @throws IncorrectCredentialsException if the username or password are incorrect.
     */
    public void login(String userName, String password)
            throws DaoException, IncorrectCredentialsException {
        
        User user = userDao.getUser(userName);
        
        if(user == null)
            throw new IncorrectCredentialsException();
        if(password.compareTo(user.getPassword()) != 0)
            throw new IncorrectCredentialsException();
        
        this.user = user;
    }
    
    
    /**
     * Attempts to register a new user with the specified information.
     * @return true if registration was successful, false otherwise.
     * @throws DaoException if there was a problem with database access.
     * @throws UsernameTakenException if the username is already taken.
     */
    public void register(User user)
            throws DaoException, UsernameTakenException {
        
        if(userDao.getUser(user.getUsername()) != null)
            throw new UsernameTakenException(); // username already taken
        
        userDao.addUser(user);
        this.user = user;
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
    
    
    public boolean isNotLoggedIn(){
        return !this.isLoggedIn();
    }
    
}
