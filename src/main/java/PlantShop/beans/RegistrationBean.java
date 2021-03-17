package PlantShop.beans;

import PlantShop.entities.ShoppingCart;
import PlantShop.entities.User;
import PlantShop.exceptions.DaoException;
import PlantShop.exceptions.UsernameTakenException;
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
        
        User user = new User();
        
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmailAddress(emailAddress);
        user.setCity(city);
        user.setStreet(street);
        user.setHouseNumber(houseNumber);
        user.setAppartment(appartment);
        user.setBirthdayDate(java.sql.Date.valueOf(birthdayDate));
        try{
            user.setPhoneNumber(Long.parseLong(phoneNumber));
            user.setZipcode(Long.parseLong(zipcode));
        } catch(NumberFormatException e){
            e.printStackTrace();
            registrationViewBean.displayFormSubmissionErrorMessage(
                    "Phone number and zipcode must be a number!");
            return null;
        }
        user.setShoppingCart(new ShoppingCart());
        user.setAdmin(false); // TODO need to set up mechanism determining when will the user be admin
        
        
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

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
    
}
