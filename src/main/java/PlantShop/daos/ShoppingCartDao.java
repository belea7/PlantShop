/**
 * DAO for shopping cart of a user in the store.
 */
package PlantShop.daos;

import PlantShop.entities.Plant;
import PlantShop.entities.PlantInCart;
import PlantShop.entities.ShoppingCart;
import PlantShop.entities.User;
import PlantShop.exceptions.DaoException;

import java.io.Serializable;
import java.util.ArrayList; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.sql.DataSource;

@DataSourceDefinition(
    name = "java:global/jdbc/PlantShop",
    className = "org.apache.derby.jdbc.ClientDataSource",
    url = "jdbc:derby://localhost:1527/PlantShop",
    databaseName = "PlantShop",
    user = "root",
    password = "root")

/**
 * DAO for shopping carts of users.
 * Contains all interactions with DB concerning the carts of the users.
 * 
 * @author Lea Ben Zvi
 */
@Named("shoppingCartDao")
@Dependent
public class ShoppingCartDao implements Serializable{
    
    // Allow the server to inject the DataSource
    @Resource(lookup="java:global/jdbc/PlantShop")
    DataSource dataSource;
    
    /**
     * Fetches all the plants in the shopping cart of the user from DB.
     * 
     * @param user
     * @return cart object
     * @throws DaoException
     */
    public ShoppingCart getCart(User user) throws DaoException{
        ShoppingCart cart = new ShoppingCart(user);
        // Connect to DB
        try (Connection connection = dataSource.getConnection()) {
            // Create SELECT statement and execute it
            String sql = "SELECT * "
                       + "FROM shopping_carts, plants "
                       + "WHERE user_name = '" + user.getUsername() + "'"
                       + " AND shopping_carts.plant_id = plants.id";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            // Fill the list of plants in cart
            while(result.next()) {
                // Create Plant object and fill it's attributes
                Plant plant = new Plant();
                plant.setId(result.getObject(4, Long.class));
                plant.setName(result.getObject(5, String.class));
                plant.setNumberOfItems(result.getObject(6, Integer.class));
                plant.setLight(result.getObject(7, String.class));
                plant.setWater(result.getObject(8, String.class));
                plant.setFertilize(result.getObject(9, String.class));
                plant.setDifficulty(result.getObject(10, String.class));
                plant.setDescription(result.getObject(11, String.class));
                plant.setPicture(result.getObject(12, String.class));
                plant.setPrice(result.getObject(13, Double.class));
                
                // Create PlantInCart and fill it's attributes
                int amount = result.getObject(3, Integer.class);
                PlantInCart plantInCart = new PlantInCart(plant, amount);
                cart.addToCart(plantInCart);
            }
            connection.close();
            
        } catch (SQLException e) { 
            e.printStackTrace();
            throw new DaoException();
        }
        return cart;
    }
    
    /**
     * Add plant to a user's cart in the DB.
     * 
     * @param plant 
     * @param cart 
     * @throws DaoException
     */
    public void addPlantToCart(PlantInCart plant, ShoppingCart cart) throws DaoException{
        // Connect to DB
        try (Connection connection = dataSource.getConnection()) {
            // Create INSERT statement
            String sql = "INSERT INTO shopping_carts "
                       + "(user_name, plant_id, amount) "
                       + "VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            // Set statemtn's variables
            statement.setString(1, cart.getUser().getUsername());
            statement.setLong(2, plant.getPlant().getId());
            statement.setInt(3, 1);
            
            // Execute query and close connection
            statement.executeUpdate();
            connection.close();
            
        } catch (SQLException e) { 
            e.printStackTrace();
            throw new DaoException();
        }
    }
    
    /**
     * Remove plant from cart in DB.
     * 
     * @param plant 
     * @param cart 
     * @throws DaoException
     */
    public void removePlantFromCart (PlantInCart plant, ShoppingCart cart) throws DaoException{
        // Connect to DB
        try (Connection connection = dataSource.getConnection()) {
            // Create DELETE statement
            String sql = "DELETE FROM shopping_carts "
                       + "WHERE plant_id = ? AND user_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, plant.getPlant().getId());
            statement.setString(2, cart.getUser().getUsername());
            
            // Execute query and close connection
            statement.executeUpdate();
            connection.close();
            
        } catch (SQLException e) { 
            e.printStackTrace();
            throw new DaoException();
        }
    }
    
    /**
     * Remove group of plants from cart in DB.
     * 
     * @param plants 
     * @param cart 
     * @throws DaoException
     */
    public void removePlantsFromCart(ArrayList<PlantInCart> plants, 
            ShoppingCart cart) throws DaoException{
        for (PlantInCart p : plants) {
            removePlantFromCart(p, cart);
        }
    }
    
    /**
     * Save amount of a certain plant in the cart of users in DB.
     * 
     * @param plant 
     * @param cart 
     * @throws DaoException
     */
    public void saveAmount (PlantInCart plant, ShoppingCart cart) throws DaoException{
        // Connect to DB
        try (Connection connection = dataSource.getConnection()) {
            // Create UPDATE statement
            String sql = "UPDATE shopping_carts "
                       + "SET amount = ? "
                       + "WHERE plant_id = ? and user_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            // Set statement's variables
            statement.setInt(1, plant.getAmount());
            statement.setLong(2, plant.getPlant().getId());
            statement.setString(3, cart.getUser().getUsername());
            
            // Execute query and close connection
            statement.executeUpdate();
            connection.close();
            
        } catch (SQLException e) { 
            e.printStackTrace();
            throw new DaoException();
        }
    }
    
    /**
     * Updates carts after performing an order.
     * 
     * @throws DaoException 
     */
    public void updateAllCarts() throws DaoException {
        // Connect to DB
        try (Connection connection = dataSource.getConnection()) {
            // Remove plants that are not in stock from all carts
            String sql = "DELETE shopping_carts "
                       + "FROM shopping_carts "
                       + "INNER JOIN plants ON plants.id = shopping_carts.plant_id "
                       + "WHERE plants.number_of_items = 0";
            Statement statement = connection.createStatement();
            statement.executeQuery(sql);
            
            // Update all plants in carts whose amount is larger than the number of items in stock
            sql = "UPDATE shopping_carts "
                + "INNER JOIN plants ON plants.id = shopping_carts.plant_id "
                + "SET shopping_carts.amount = plants.number_of_items "
                + "WHERE plants.number_of_items < shopping_carts.amount";
            statement = connection.createStatement();
            statement.executeQuery(sql);
        } catch (SQLException e) { 
            e.printStackTrace();
            throw new DaoException();
        }
    }
}