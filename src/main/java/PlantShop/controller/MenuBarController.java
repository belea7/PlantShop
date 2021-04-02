/*
 * Menubar Controller.
 */
package PlantShop.controller;

import PlantShop.model.UserModel;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Controller for menu bar in the application.
 * 
 * @author leagi
 */
@Named(value = "menuBarController")
@ViewScoped
public class MenuBarController implements Serializable{
    
    @Inject
    private UserModel userModel;
    
    /**
     * Is user logged in.
     * 
     * @return logged-in or not
     */
    public boolean isLoggedIn() {
        return userModel.isLoggedIn();
    }
    
    /**
     * Is the connected user an admin.
     * 
     * @return is admin or not
     */
    public boolean isAdmin() {
        return userModel.isAdmin();
    }
    
    /**
     * Tells UserBean to logout.
     * 
     * @return page to navigate to after logout.
     */
    public String logout() {
        
        userModel.logOut();
        return "logged_out";
    }
}
