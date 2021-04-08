package PlantShop.controller;

import PlantShop.entities.User;
import PlantShop.exceptions.ProfileInputFormatException;
import PlantShop.util.ErrorDisplay;
import java.io.Serializable;

/**
 * Abstract class representing a controller bean that can receive data about
 * a user's profile from input components, and parse it into a ProfileDetails object.
 * @author Ron Mosenzon
 */
public abstract class AbstractProfileDataInputController implements Serializable {
    
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
    private String birthDate = "";
    
    
    /**
     * @return the user name received through this class's setter method.
     */
    protected String getInputUserName() {
        return username;
    }
    
    
    /**
     * Parse the profile details received through this class's setter methods into a user object.
     * @param errorDisplay error display for displaying format error messages.
     * @return a User object representing the profile details received through this class's setter methods.
     * @throws ProfileInputFormatException if there were any format errors.
     */
    protected User.ProfileDetails parseInputDetails(ErrorDisplay errorDisplay) throws ProfileInputFormatException {
        
        boolean parseError = false; // whether there was a parsing error
        
        User.ProfileDetails profile = new User.ProfileDetails(); // user object for storing parsed info
        
        // parse inputs
        profile.setPassword(password);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setEmailAddress(emailAddress);
        profile.setCity(city);
        profile.setStreet(street);
        try{
            profile.setBirthDate(java.sql.Date.valueOf(birthDate));
        } catch(IllegalArgumentException e) {
            errorDisplay.displayErrorMessage(
                    "Birth Date must be of the form 'yyyy-mm-dd'");
            parseError = true;
        }
        profile.setPhoneNumber(phoneNumber);
        if( !phoneNumber.matches("[0-9]+") ){ // if phone number contains none-digit characters
            errorDisplay.displayErrorMessage(
                    "Phone number must be a number");
            parseError = true;
        }
        try{
            profile.setHouseNumber(Integer.parseInt(houseNumber));
        } catch(NumberFormatException e){
            errorDisplay.displayErrorMessage(
                    "House Number must be a number");
            parseError = true;
        }
        try{
            profile.setAppartment(Integer.parseInt(appartment));
        } catch(NumberFormatException e){
            errorDisplay.displayErrorMessage(
                    "Apartment must be a number");
            parseError = true;
        }
        try{
            profile.setZipcode(Long.parseLong(zipcode));
        } catch(NumberFormatException e){
            errorDisplay.displayErrorMessage(
                    "Zipcode must be a number");
            parseError = true;
        }
        
        if(parseError == true)
            throw new ProfileInputFormatException("Error when parsing profile details");
        
        return profile;
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    
}