/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlantShop.daos;

import PlantShop.entities.Plant;
import PlantShop.entities.Review;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.inject.Named;

/**
 *
 * @author leagi
 */
@Named("reviewsDao")
public class ReviewsDao implements Serializable{
    private String url = "jdbc:derby://localhost:1527/PlantShop";
    private String user = "root";
    private String password = "root";
    
    public ArrayList<Review> getReviews(Plant plant) {
        ArrayList<Review> reviews = new ArrayList();
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Execute get statement for all plants
            String sql = "SELECT * FROM Reviews WHERE plant_id = " + plant.getId();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()) {
                Review review = new Review();
                review.setUserName(result.getObject(1, String.class));
                review.setPlantId(result.getObject(2, Integer.class));
                review.setTimePosted(result.getObject(3, Timestamp.class));
                review.setRating(result.getObject(4, Integer.class));
                review.setComment(result.getObject(5, String.class));
                reviews.add(review);
            }
            connection.close();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
        return reviews;
    }
    
    public void addReview(Review review) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Execute get statement for all plants
            String sql = "INSERT INTO reviews (user_name, plant_id, time_posted, "
                    + "rating, comment) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, review.getUserName());
            statement.setInt(2, review.getPlantId());
            statement.setTimestamp(3, review.getTimePosted());
            statement.setInt(4, review.getRating());
            statement.setString(5, review.getComment());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
    public void updateReview(Review review) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Execute get statement for all plants
            String sql = "UPDATE reviws SET rating = ?, comment = ? "
                    +  "WHERE user_name = ? AND plant_id = ? AND time_posted = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, review.getRating());
            statement.setString(2, review.getComment());
            statement.setString(3, review.getUserName());
            statement.setInt(4, review.getPlantId());
            statement.setTimestamp(5, review.getTimePosted());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
    public void removeReview(Review review) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Execute get statement for all plants
            String sql = "DELETE FROM reviws "
                    + "WHERE user_name = ?, plant_id = ?, time_posted = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, review.getUserName());
            statement.setInt(2, review.getPlantId());
            statement.setTimestamp(2, review.getTimePosted());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
    public void removeReviews(ArrayList<Review> reviews) {
        for (Review r: reviews) {
            removeReview(r);
        }
    }
}
