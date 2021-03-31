/*
 * DAO for users in the DB.
 */
package PlantShop.daos;

import PlantShop.entities.User;
import PlantShop.exceptions.DaoException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.sql.DataSource;

@DataSourceDefinition(
    name = "java:global/jdbc/plantShop",
    className = "org.apache.derby.jdbc.ClientDataSource",
    url = "jdbc:derby://localhost:1527/plantShop",
    databaseName = "plantShop",
    user = "root",
    password = "root")

/**
 * A class that abstracts database queries about User objects.
 * @author Ron Mosenzon
 */
@Named(value = "userDao")
@Dependent
public class UserDao implements Serializable {
    
    @Inject
    private ShoppingCartDao shoppingCartDao;
    
    // allow the server to inject the DataSource
    @Resource(lookup="java:global/jdbc/plantShop")
    DataSource dataSource;
    
    
    /**
     * Searches the database for a user with the specified username.
     * @param username the target username.
     * @return the user that was found, or null if no such user was found.
     * @throws DaoException if there was a problem with database access.
     */
    public User getUser(String username) throws DaoException {
        
        User user = null;
        
        try (Connection connection = dataSource.getConnection()) {
            // Execute get statement for user
            String sql = "SELECT * FROM users WHERE user_name='" + username + "'";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            if(result.next()) {
                user = new User();
                user.setUsername(username);
                user.setPassword(result.getObject("password", String.class));
                user.setFirstName(result.getObject("first_name", String.class));
                user.setLastName(result.getObject("last_name", String.class));
                user.setPhoneNumber(result.getObject("phone_number", Long.class));
                user.setEmailAddress(result.getObject("email_address", String.class));
                user.setCity(result.getObject("city", String.class));
                user.setStreet(result.getObject("street", String.class));
                user.setHouseNumber(result.getObject("house_number", String.class));
                user.setAppartment(result.getObject("apartment", String.class));
                user.setZipcode(result.getObject("zip_code", Long.class));
                user.setBirthdayDate(result.getObject("birthday_date", java.sql.Date.class));
                user.setAdmin(result.getObject("admin", Boolean.class));
                user.setShoppingCart(shoppingCartDao.getCart(user));
            }
            
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException();
        }
        return user;
    }
    
    
    /**
     * Adds the specified user to the database.
     * @param user the user that will be added to the database.
     * @throws DaoException if there was a problem with database access.
     */
    public void addUser(User user) throws DaoException {
        
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO users(user_name, first_name, last_name"
                    + ", phone_number, email_address, city, street, house_number"
                    + ", apartment, zip_code, birthday_date, admin, password)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setLong(4, user.getPhoneNumber());
            statement.setString(5, user.getEmailAddress());
            statement.setString(6, user.getCity());
            statement.setString(7, user.getStreet());
            statement.setString(8, user.getHouseNumber());
            statement.setString(9, user.getAppartment());
            statement.setLong(10, user.getZipcode());
            statement.setDate(11, user.getBirthdayDate());
            statement.setBoolean(12, user.isAdmin());
            statement.setString(13, user.getPassword());
            statement.execute();
            
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException();
        }
        
    }
    
    
}
