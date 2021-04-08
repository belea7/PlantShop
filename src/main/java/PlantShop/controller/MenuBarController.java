/*
 * Menubar controller
 */
package PlantShop.controller;

import PlantShop.model.UserModel;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Controller bean providing the methods used by the on-screen menu bar.
 * @author Lea Ben Zvi
 */
@Named(value = "menuBarController")
@ViewScoped
public class MenuBarController implements Serializable{
    
    @Inject
    private UserModel userBean;
    
    /**
     * Is user logged in.
     * 
     * @return logged-in or not
     */
    public boolean isLoggedIn() {
        return userBean.isLoggedIn();
    }
    
    /**
     * Is the connected user an admin.
     * 
     * @return is admin or not
     */
    public boolean isAdmin() {
        return userBean.isAdmin();
    }
    
    /**
     * Tells UserModel to logout.
     * 
     * @return page to navigate to after logout.
     */
    public String logout() {
        
        userBean.logOut();
        return "index";
    }
}