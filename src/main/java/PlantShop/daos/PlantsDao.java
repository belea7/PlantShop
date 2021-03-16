/*
 * DAO for plants in store.
 */
package PlantShop.daos;

import PlantShop.entities.Plant;

import java.util.ArrayList; 
import java.io.Serializable;
import javax.inject.Named;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.enterprise.context.Dependent;
import javax.sql.DataSource;

@DataSourceDefinition(
    name = "java:global/jdbc/plantShop",
    className = "org.apache.derby.jdbc.ClientDataSource",
    url = "jdbc:derby://localhost:1527/plantShop",
    databaseName = "plantShop",
    user = "root",
    password = "root")

/**
 * DAO for plants in the store.
 * Contains interactions with DB concerning the plants in the store.
 * 
 * @author leagi
 */
@Named("plantsDao")
@Dependent
public class PlantsDao implements Serializable{
    private ReviewsDao reviewsDao = new ReviewsDao();               // Reviews DAO
    
    // allow the server to inject the DataSource
    @Resource(lookup="java:global/jdbc/plantShop")
    DataSource dataSource;
    
    /**
     * Fetches all the plants in the DB.
     * For each plant, it's reviews are fetched as well.
     * 
     * @return list of plants in store
     */
    public ArrayList<Plant> getPlants(){
        ArrayList<Plant> plants = new ArrayList();
        
        // Fetch plants from DB
        try (Connection connection = dataSource.getConnection()) {
            // Execute get statement for all plants
            String sql = "SELECT *"
                       + "FROM plants";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            // Fill the list of plants
            while(result.next()) {
                // Create plant entity and set its' attributes
                Plant plant = new Plant();
                plant.setId(result.getObject(1, Integer.class));
                plant.setName(result.getObject(2, String.class));
                plant.setNumberOfItems(result.getObject(3, Integer.class));
                plant.setLight(result.getObject(4, String.class));
                plant.setWater(result.getObject(5, String.class));
                plant.setFertilize(result.getObject(6, String.class));
                plant.setDifficulty(result.getObject(7, String.class));
                plant.setDescription(result.getObject(8, String.class));
                plant.setPicture(result.getObject(9, String.class));
                plant.setPrice(result.getObject(10, Integer.class));
                plant.setReviews(reviewsDao.getReviews(plant));
                plants.add(plant);
            }
            connection.close();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
        return plants;
    }
    
    /**
     * Add plant to DB.
     * 
     * @param plant 
     */
    public void addPlant(Plant plant) {
        // Connecto to DB
        try (Connection connection = dataSource.getConnection()) {
            // Insert the plant to the DB
            String sql = "INSERT INTO plants"
                    + "(id, name, number_of_items, light, water, "
                    + "fertilize, difficulty, description, picture, price)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            // Set statement variables
            statement.setInt(1, plant.getId());
            statement.setString(2, plant.getName());
            statement.setInt(3, plant.getNumberOfItems());
            statement.setString(4, plant.getLight());
            statement.setString(5, plant.getWater());
            statement.setString(6, plant.getFertilize());
            statement.setString(7, plant.getDifficulty());
            statement.setString(8, plant.getDescription());
            statement.setString(9, plant.getPicture());
            statement.setInt(10, plant.getPrice());
            
            // Execute query and close conntection
            statement.executeUpdate();
            connection.close();
            
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
    /**
     * Update plant record in the DB.
     * 
     * @param plant 
     */
     public void updatePlant(Plant plant) {
         // Connetct to DB
        try (Connection connection = dataSource.getConnection()) {
            // Create update statement
            String sql = "UPDATE plants "
                    + "SET name = ?, number_of_items = ?, light = ?, water = ?, "
                    + "fertilize = ?, difficulty = ?, description = ?, "
                    + "picture = ?, price = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            // Set statement's variables
            statement.setString(1, plant.getName());
            statement.setInt(2, plant.getNumberOfItems());
            statement.setString(3, plant.getLight());
            statement.setString(4, plant.getWater());
            statement.setString(5, plant.getFertilize());
            statement.setString(6, plant.getDifficulty());
            statement.setString(7, plant.getDescription());
            statement.setString(8, plant.getPicture());
            statement.setInt(9, plant.getPrice());
            statement.setInt(10, plant.getId());
            
            // Execute query and close connection
            statement.executeUpdate();
            connection.close();
            
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
     /**
      * Remove plant record from DB.
      * 
      * @param plant 
      */
    public void removePlant(Plant plant) {
        // Connect to DB
        try (Connection connection = dataSource.getConnection()) {
            // Create DELETE statement
            String sql = "DELETE FROM plants "
                       + "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, plant.getId());
            
            // Execute query and close connection
            statement.executeUpdate();
            connection.close();
            
            // Remove the reviews on the plant
            reviewsDao.removeReviews(plant.getReviews());
            
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
    /**
     * Remove group of plants from DB.
     * 
     * @param plants 
     */
    public void removePlants(ArrayList<Plant> plants) {
        for (Plant p : plants) {
            removePlant(p);
        }
    }
}
