/**
 * DAO for shopping cart of a user in the store.
 */
package PlantShop.daos;

import PlantShop.entities.Plant;
import PlantShop.entities.PlantInCart;
import PlantShop.entities.ShoppingCart;

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
    name = "java:global/jdbc/plantShop",
    className = "org.apache.derby.jdbc.ClientDataSource",
    url = "jdbc:derby://localhost:1527/plantShop",
    databaseName = "plantShop",
    user = "root",
    password = "root")

/**
 * DAO for shopping carts of users.
 * Contains all interactions with DB concerning the carts of the users.
 * 
 * @author leagi
 */
@Named("shoppingCartDao")
@Dependent
public class ShoppingCartDao implements Serializable{
    
    // allow the server to inject the DataSource
    @Resource(lookup="java:global/jdbc/plantShop")
    DataSource dataSource;
    
    /**
     * Fetches all the plants in the shopping cart of the user from DB.
     * 
     * @return 
     */
    public ArrayList<PlantInCart> getPlants(){
        ArrayList<PlantInCart> plants = new ArrayList();
        // Connect to DB
        try (Connection connection = dataSource.getConnection()) {
            // Create SELECT statement and execute it
            String sql = "SELECT * "
                       + "FROM shoppingcarts, plants "
                       + "WHERE user_name='admin' AND shoppingcarts.plant_id = plants.id";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            // Fill the list of plants in cart
            while(result.next()) {
                // Create Plant object and fill it's attributes
                Plant plant = new Plant();
                plant.setId(result.getObject(4, Integer.class));
                plant.setName(result.getObject(5, String.class));
                plant.setNumberOfItems(result.getObject(6, Integer.class));
                plant.setLight(result.getObject(7, String.class));
                plant.setWater(result.getObject(8, String.class));
                plant.setFertilize(result.getObject(9, String.class));
                plant.setDifficulty(result.getObject(10, String.class));
                plant.setDescription(result.getObject(11, String.class));
                plant.setPicture(result.getObject(12, String.class));
                plant.setPrice(result.getObject(13, Integer.class));
                
                // Create PlantInCart and fill it's attributes
                int amount = result.getObject(3, Integer.class);
                PlantInCart plantInCart = new PlantInCart(plant, amount);
                plants.add(plantInCart);
            }
            connection.close();
            
        } catch (SQLException e) { 
            e.printStackTrace();
        }
        return plants;
    }
    
    /**
     * Fetches the shopping cart of the user from DB.
     * 
     * @return 
     */
    public ShoppingCart getCart() {
        ShoppingCart cart = new ShoppingCart();
        cart.setItems(this.getPlants());
        return cart;
    }
    
    /**
     * Add plant to a user's cart in the DB.
     * 
     * @param plant 
     */
    public void addPlantToCart(Plant plant) {
        // Connect to DB
        try (Connection connection = dataSource.getConnection()) {
            // Create INSERT statement
            String sql = "INSERT INTO ShoppingCarts "
                       + "(user_name, plant_id, amount) "
                       + "VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            // Set statemtn's variables
            statement.setString(1, "admin");
            statement.setInt(2, plant.getId());
            statement.setInt(3, 1);
            
            // Execute query and close connection
            statement.executeUpdate();
            connection.close();
            
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
    /**
     * Remove plant from cart in DB.
     * 
     * @param plant 
     */
    public void removePlantFromCart (PlantInCart plant) {
        // Connect to DB
        try (Connection connection = dataSource.getConnection()) {
            // Create DELETE statement
            String sql = "DELETE FROM ShoppingCarts "
                       + "WHERE plant_id = ? AND user_name = 'admin'";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, plant.getPlant().getId());
            
            // Execute query and close connection
            statement.executeUpdate();
            connection.close();
            
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
    /**
     * Remove group of plants from cart in DB.
     * 
     * @param plants 
     */
    public void removePlantsFromCart(ArrayList<PlantInCart> plants) {
        for (PlantInCart p : plants) {
            removePlantFromCart(p);
        }
    }
    
    /**
     * Save amount of a certain plant in the cart of users in DB.
     * 
     * @param plant 
     */
    public void saveAmount (PlantInCart plant) {
        // Connect to DB
        try (Connection connection = dataSource.getConnection()) {
            // Create UPDATE statement
            String sql = "UPDATE ShoppingCarts "
                       + "SET amount = ? "
                       + "WHERE plant_id = ? and user_name = 'admin'";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            // Set statement's variables
            statement.setInt(1, plant.getAmount());
            statement.setInt(2, plant.getPlant().getId());
            
            // Execute query and close connection
            statement.executeUpdate();
            connection.close();
            
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
}
