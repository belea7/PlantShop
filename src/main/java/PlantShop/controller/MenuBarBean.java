/*
 * Menubar bean
 */
package PlantShop.controller;

import PlantShop.model.UserBean;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author leagi
 */
@Named(value = "menuBarBean")
@ViewScoped
public class MenuBarBean implements Serializable{
    
    @Inject
    private UserBean userBean;
    
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
     * Tells UserBean to logout.
     * 
     * @return page to navigate to after logout.
     */
    public String logout() {
        
        userBean.logOut();
        return "logged_out";
    }
}
