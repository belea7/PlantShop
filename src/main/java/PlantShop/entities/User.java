/*
 * Contains user entity class.
 */
package PlantShop.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

/**
 * Entity representing a user.
 * @author leagi
 */
public class User implements Serializable {
    private String username;
    private ProfileDetails profileDetails = new ProfileDetails();
    private boolean admin;
    private ShoppingCart shoppingCart;
    
    public User() {}
    
    /**
     * Create a user object with the specified username.
     * @param username the username for the user object.
     */
    public User(String username) {
        this.username = username;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.username);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return profileDetails.firstName;
    }

    public void setFirstName(String firstName) {
        this.profileDetails.firstName = firstName;
    }

    public String getLastName() {
        return profileDetails.lastName;
    }

    public void setLastName(String lastName) {
        this.profileDetails.lastName = lastName;
    }

    public String getPhoneNumber() {
        return profileDetails.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.profileDetails.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return profileDetails.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.profileDetails.emailAddress = emailAddress;
    }

    public String getCity() {
        return profileDetails.city;
    }

    public void setCity(String city) {
        this.profileDetails.city = city;
    }

    public String getStreet() {
        return profileDetails.street;
    }

    public void setStreet(String street) {
        this.profileDetails.street = street;
    }

    public int getHouseNumber() {
        return profileDetails.houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.profileDetails.houseNumber = houseNumber;
    }

    public int getAppartment() {
        return profileDetails.appartment;
    }

    public void setAppartment(int appartment) {
        this.profileDetails.appartment = appartment;
    }

    public long getZipcode() {
        return profileDetails.zipcode;
    }

    public void setZipcode(long zipcode) {
        this.profileDetails.zipcode = zipcode;
    }

    public Date getBirthdayDate() {
        return profileDetails.birthdayDate;
    }

    public void setBirthdayDate(Date birthdayDate) {
        this.profileDetails.birthdayDate = birthdayDate;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return profileDetails.password;
    }

    public void setPassword(String password) {
        this.profileDetails.password = password;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public ProfileDetails getProfileDetails() {
        return profileDetails;
    }

    public void setProfileDetails(ProfileDetails profileDetails) {
        if(profileDetails == null){
            System.err.println("setProfileDetails called with null argument");
            this.profileDetails = new ProfileDetails();
            return;
        }
        this.profileDetails = profileDetails;
    }
    
    
    /**
     * Class representing the profile details of a user.
     * @author Ron Mosenzon
     */
    public static class ProfileDetails {
        
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String emailAddress;
        private String city;
        private String street;
        private int houseNumber;
        private int appartment;
        private long zipcode;
        private Date birthdayDate;
        private String password;
        
        /**
         * Creates a new ProfileDetails object with empty fields
         */
        public ProfileDetails(){}

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
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

        public int getHouseNumber() {
            return houseNumber;
        }

        public void setHouseNumber(int houseNumber) {
            this.houseNumber = houseNumber;
        }

        public int getAppartment() {
            return appartment;
        }

        public void setAppartment(int appartment) {
            this.appartment = appartment;
        }

        public long getZipcode() {
            return zipcode;
        }

        public void setZipcode(long zipcode) {
            this.zipcode = zipcode;
        }

        public Date getBirthdayDate() {
            return birthdayDate;
        }

        public void setBirthdayDate(Date birthdayDate) {
            this.birthdayDate = birthdayDate;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
        
    }
    
}