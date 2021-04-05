package PlantShop.model;

import PlantShop.daos.UserDao;
import PlantShop.entities.ShoppingCart;
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
@Named(value = "userModel")
@SessionScoped
public class UserModel implements Serializable {
    
    /**
     * Information about the user that is currently logged in.
     * If no user is currently logged in, then this should be null.
     */
    private User user = null;
    
    @Inject
    private UserDao userDao;
    
    @Inject
    private ShoppingCartModel shoppingCartModel;

    /**
     * Creates a new instance of UserBean.
     */
    public UserModel() {
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
        shoppingCartModel.setCart(user.getShoppingCart());
    }
    
    
    /**
     * Attempts to register a new user with the specified username and profile information.
     * @param userName the name of the new user to be registered.
     * @param details profile details of the new user to be registered.
     * @param isAdmin whether the new user should be an admin.
     * @return true if registration was successful, false otherwise.
     * @throws DaoException if there was a problem with database access.
     * @throws UsernameTakenException if the username is already taken.
     */
    public void register(String userName, User.ProfileDetails details, boolean isAdmin)
            throws DaoException, UsernameTakenException {
        
        if(userDao.getUser(userName) != null)
            throw new UsernameTakenException(); // username already taken
        
        User user = new User(userName); // user entity representing the new user
        user.setProfileDetails(details);
        user.setAdmin(isAdmin);
        user.setShoppingCart(new ShoppingCart(user));
        
        userDao.addUser(user);
        this.user = user;
    }
    
    
    /**
     * Attempts to edit the profile of the currently logged in user, by
     * changing the username and details to the ones received in the arguments,
     * and also updating the database accordingly.
     * @param newUserName the new username that the user will have.
     * @param newDetails new profile details for the user.
     * @throws DaoException if there was a problem with database access.
     * @throws UsernameTakenException If the new username is already taken,
     * in which case none of the edits done by this method take affect.
     */
    public void editProfile(String newUserName, User.ProfileDetails newDetails)
            throws DaoException, UsernameTakenException {
        
        if( !this.user.getUsername().equals(newUserName) )
            if(userDao.getUser(newUserName) != null)
                throw new UsernameTakenException(); // username already taken
        
        userDao.editUser(this.user.getUsername(), newUserName, newDetails);
        this.user.setUsername(newUserName);
        this.user.setProfileDetails(newDetails);
        
    }
    
    
    /**
     * Logs out the user.
     */
    public void logOut() {
        
        user = null;
        
    }

    
    /**
     * @return the currently logged-in user, or null if no user is currently logged in.
     */
    public User getUser() {
        return user;
    }
    
    
    /**
     * @return true if the user is currently logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return (user != null);
    }
    
    
    /**
     * @return false if the user is currently logged in, true otherwise.
     */
    public boolean isNotLoggedIn(){
        return !this.isLoggedIn();
    }
    
    
    /**
     * @return true if the user is currently logged in on an admin account, false otherwise.
     */
    public boolean isAdmin(){
        if(user == null)
            return false;
        
        return user.isAdmin();
    }
    
    
    /**
     * @return false if the user is currently logged in on an admin account, true otherwise.
     */
    public boolean isNotAdmin(){
        return !this.isAdmin();
    }
    
}