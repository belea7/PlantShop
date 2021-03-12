package PlantShop.daos;

import PlantShop.entities.Plant;
import PlantShop.entities.PlantInCart;
import java.io.Serializable;
import java.util.ArrayList; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.inject.Named;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author leagi
 */
@Named("shoppingCartDao")
public class ShoppingCartDao implements Serializable{
    private String url = "jdbc:derby://localhost:1527/PlantShop";
    private String user = "root";
    private String password = "root";
    
    public ArrayList<PlantInCart> getPlants(){
        ArrayList<PlantInCart> plants = new ArrayList();
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Execute get statement for all plants
            String sql = "SELECT * FROM shoppingcarts, plants WHERE user_name='admin'"
                    + " AND shoppingcarts.plant_id = plants.id";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()) {
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
    
    public void addPlantToCart(Plant plant) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Execute get statement for all plants
            String sql = "INSERT INTO ShoppingCarts (user_name, plant_id, amount)"
                    + " VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "admin");
            statement.setInt(2, plant.getId());
            statement.setInt(3, 1);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
    public void removePlantsFromCart(ArrayList<PlantInCart> plants) {
        for (PlantInCart p : plants) {
            removePlantFromCart(p);
        }
    }
    
    public void removePlantFromCart (PlantInCart plant) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Execute get statement for all plants
            String sql = "DELETE FROM ShoppingCarts WHERE plant_id = ? AND user_name = 'admin'";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, plant.getPlant().getId());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
    public void saveAmount (PlantInCart plant) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Execute get statement for all plants
            String sql = "UPDATE ShoppingCarts SET amount = ? "
                    + "WHERE plant_id = ? and user_name = 'admin'";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, plant.getAmount());
            statement.setInt(2, plant.getPlant().getId());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
}
