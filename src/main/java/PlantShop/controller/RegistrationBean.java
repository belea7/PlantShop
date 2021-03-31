package PlantShop.controller;

import PlantShop.entities.ShoppingCart;
import PlantShop.entities.User;
import PlantShop.exceptions.DaoException;
import PlantShop.exceptions.UsernameTakenException;
import PlantShop.model.UserBean;
import PlantShop.view.RegistrationViewBean;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 * Bean that gets the user's information from registration
 * page and puts it into the database.
 * @author Ron Mosenzon
 */
@Named(value = "registrationBean")
@ViewScoped
public class RegistrationBean implements Serializable {
    
    private final String ADMIN_CODE = "admin";
    
    private String firstName = "";
    private String lastName = "";
    private String phoneNumber = "";
    private String username = "";
    private String password = "";
    private String emailAddress = "";
    private String city = "";
    private String street = "";
    private String houseNumber = "";
    private String appartment = "";
    private String zipcode = "";
    private String birthdayDate = "";
    private String admin = "";
    
    @Inject
    private RegistrationViewBean registrationViewBean;
    
    @Inject
    private UserBean userBean;
    
    public RegistrationBean() {
        
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
        
        boolean parseError = false; // whether there was a parsing error
        
        User user = new User(); // user object for storing parsed registration info
        
        // parse inputs
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmailAddress(emailAddress);
        user.setCity(city);
        user.setStreet(street);
        user.setHouseNumber(houseNumber);
        user.setAppartment(appartment);
        try{
            user.setBirthdayDate(java.sql.Date.valueOf(birthdayDate));
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            registrationViewBean.displayFormSubmissionErrorMessage(
                    "Birth Date must be of the form 'yyyy-mm-dd'");
            parseError = true;
        }
        try{
            user.setPhoneNumber(Long.parseLong(phoneNumber));
        } catch(NumberFormatException e){
            e.printStackTrace();
            registrationViewBean.displayFormSubmissionErrorMessage(
                    "Phone number must be a number");
            parseError = true;
        }
        try{
            user.setZipcode(Long.parseLong(zipcode));
        } catch(NumberFormatException e){
            e.printStackTrace();
            registrationViewBean.displayFormSubmissionErrorMessage(
                    "Zipcode must be a number");
            parseError = true;
        }
        user.setShoppingCart(new ShoppingCart(user));
        user.setAdmin(admin.compareTo(ADMIN_CODE) == 0);
        
        if(parseError == true)
            return null; // Do not try to register if there was a parsing error
        
        
        // register new user
        try{
            userBean.register(user);
        } catch(DaoException e) {
            registrationViewBean.displayFormSubmissionErrorMessage(
                    "Oops! Something went wrong when connecting to the database.");
            return null;
        } catch(UsernameTakenException e) {
            registrationViewBean.displayFormSubmissionErrorMessage(
                    "That username is already taken!");
            return null;
        }
        
        // return navigation result
        return "registered";
    }
    
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getAppartment() {
        return appartment;
    }

    public void setAppartment(String appartment) {
        this.appartment = appartment;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(String birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
    
}
