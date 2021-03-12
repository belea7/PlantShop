/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlantShop.daos;

import PlantShop.entities.Plant;
import PlantShop.entities.Review;

import java.util.ArrayList; 
import java.io.Serializable;
import javax.inject.Named;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.inject.Inject;

/**
 *
 * @author leagi
 */
@Named("plantsDao")
public class PlantsDao implements Serializable{
    private String url = "jdbc:derby://localhost:1527/PlantShop";
    private String user = "root";
    private String password = "root";
    private ReviewsDao reviewsDao = new ReviewsDao();
    
    public ArrayList<Plant> getPlants(){
        ArrayList<Plant> plants = new ArrayList();
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Execute get statement for all plants
            String sql = "SELECT * FROM plants";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()) {
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
    
    public void addPlant(Plant plant) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Execute get statement for all plants
            String sql = "INSERT INTO plants (id, name, number_of_items, light, "
                    + "water, fertilize, difficulty, description, picture,"
                    + "price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
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
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
     public void updatePlant(Plant plant) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Execute get statement for all plants
            String sql = "UPDATE plants SET "
                    + "name = ?, number_of_items = ?, light = ?, water = ?, "
                    + "fertilize = ?, difficulty = ?, description = ?, "
                    + "picture = ?, price = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
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
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
    public void removePlants(ArrayList<Plant> plants) {
        for (Plant p : plants) {
            removePlant(p);
        }
    }
    
    public void removePlant(Plant plant) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Execute get statement for all plants
            String sql = "DELETE FROM plants WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, plant.getId());
            statement.executeUpdate();
            connection.close();
            reviewsDao.removeReviews(plant.getReviews());
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
}
