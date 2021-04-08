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
    name = "java:global/jdbc/PlantShop",
    className = "org.apache.derby.jdbc.ClientDataSource",
    url = "jdbc:derby://localhost:1527/PlantShop",
    databaseName = "PlantShop",
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
    @Resource(lookup="java:global/jdbc/PlantShop")
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
                user.setPhoneNumber(result.getObject("phone_number", String.class));
                user.setEmailAddress(result.getObject("email_address", String.class));
                user.setCity(result.getObject("city", String.class));
                user.setStreet(result.getObject("street", String.class));
                user.setHouseNumber(result.getObject("house_number", Integer.class));
                user.setAppartment(result.getObject("apartment", Integer.class));
                user.setZipcode(result.getObject("zip_code", Long.class));
                user.setBirthDate(result.getObject("birth_date", java.sql.Date.class));
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
                    + ", apartment, zip_code, birth_date, admin, password)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getPhoneNumber());
            statement.setString(5, user.getEmailAddress());
            statement.setString(6, user.getCity());
            statement.setString(7, user.getStreet());
            statement.setInt(8, user.getHouseNumber());
            statement.setInt(9, user.getAppartment());
            statement.setLong(10, user.getZipcode());
            statement.setDate(11, user.getBirthDate());
            statement.setBoolean(12, user.isAdmin());
            statement.setString(13, user.getPassword());
            statement.execute();
            
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException();
        }
        
    }
    
    
    /**
     * Edit the profile of the user specified by oldUserName to have the specified new user name and profile details.
     * @param oldUserName the username of the user to be edited.
     * @param newUserName the new username that the user will have after the edit.
     * @param details new profile details that the user will have after the edit.
     * @throws DaoException if there was a problem with database access.
     */
    public void editUser(String oldUserName, String newUserName, User.ProfileDetails details)
            throws DaoException {
        
        try (Connection connection = dataSource.getConnection()) {
            
            // sql query to update users table
            String usersSql = "UPDATE users"
                    + " SET user_name = ?, first_name = ?, last_name = ?"
                    + ", phone_number = ?, email_address = ?, city = ?, street = ?"
                    + ", house_number = ?, apartment = ?, zip_code = ?, birth_date = ?"
                    + ", password = ?"
                    + " WHERE user_name = ?";
            
            // sql query to update username in orders table
            String ordersSql = "UPDATE orders"
                    + " SET user_name = ?"
                    + " WHERE user_name = ?";
            
            // sql query to update username in shopping_carts table
            String shoppingCartsSql = "UPDATE shopping_carts"
                    + " SET user_name = ?"
                    + " WHERE user_name = ?";
            
            // sql query to update username in reviews table
            String reviewsSql = "UPDATE reviews"
                    + " SET user_name = ?"
                    + " WHERE user_name = ?";
            
            /**
             * The second try statement is for performing a rollback if an
             * exception occurs during the transaction,
             * which is not possible in the catch clause of the outer try statement
             * since the connection gets auto-closed before that clause is executed.
             */
            try (PreparedStatement usersStatement = connection.prepareStatement(usersSql);
                 PreparedStatement ordersStatement = connection.prepareStatement(ordersSql);
                 PreparedStatement shoppingCartsStatement = connection.prepareStatement(shoppingCartsSql);
                 PreparedStatement reviewsStatement = connection.prepareStatement(reviewsSql)){
                
                connection.setAutoCommit(false); // begin transaction
                
                // set up and execute users statement
                usersStatement.setString(1, newUserName);
                usersStatement.setString(2, details.getFirstName());
                usersStatement.setString(3, details.getLastName());
                usersStatement.setString(4, details.getPhoneNumber());
                usersStatement.setString(5, details.getEmailAddress());
                usersStatement.setString(6, details.getCity());
                usersStatement.setString(7, details.getStreet());
                usersStatement.setInt(8, details.getHouseNumber());
                usersStatement.setInt(9, details.getAppartment());
                usersStatement.setLong(10, details.getZipcode());
                usersStatement.setDate(11, details.getBirthDate());
                usersStatement.setString(12, details.getPassword());
                usersStatement.setString(13, oldUserName);
                usersStatement.execute();
                
                // set up and execute orders statement
                ordersStatement.setString(1, newUserName);
                ordersStatement.setString(2, oldUserName);
                ordersStatement.execute();
                
                // set up and execute shopping carts statement
                shoppingCartsStatement.setString(1, newUserName);
                shoppingCartsStatement.setString(2, oldUserName);
                shoppingCartsStatement.execute();
                
                // set up and execute reviews statement
                reviewsStatement.setString(1, newUserName);
                reviewsStatement.setString(2, oldUserName);
                reviewsStatement.execute();

                connection.commit(); // commit transaction
                
            } catch(SQLException e) { // catch exception during preparation or execution of statements
                e.printStackTrace();
                try{
                    connection.rollback(); // rollback the failed transaction before closing connection
                    connection.close();
                } catch(SQLException exception) {
                    exception.printStackTrace();
                }
                throw new DaoException("error during editUser transaction");
            }
            
            connection.close();
        } catch (SQLException e) { // catch exception during opening or closing of connection
            e.printStackTrace();
            throw new DaoException("error when opening or closing the connection at editUser");
        }
        
    }
    
    
}