/*
 * DAO for reviews on plants.
 */
package PlantShop.daos;

import PlantShop.entities.Plant;
import PlantShop.entities.Review;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
 * DAO for reviews on plants.
 * Contains interactions with DB concerning the reviews on plants in the store.
 * 
 * @author leagi
 */
@Named("reviewsDao")
@Dependent
public class ReviewsDao implements Serializable{
    
    // Allow the server to inject the DataSource
    @Resource(lookup="java:global/jdbc/plantShop")
    DataSource dataSource;
    
    /**
     * Fetches all the reviews in the DB for a specific plant.
     * 
     * @param plant
     * @return list of reviews
     */
    public ArrayList<Review> getReviews(Plant plant) {
        ArrayList<Review> reviews = new ArrayList();
        // Connect to DB
        try (Connection connection = dataSource.getConnection()) {
            // Execute SELECT statement
            String sql = "SELECT * "
                       + "FROM Reviews "
                       + "WHERE plant_id = " + plant.getId();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            // Fill the list of reviews
            while(result.next()) {
                Review review = new Review();
                review.setUserName(result.getObject(1, String.class));
                review.setTimePosted(result.getObject(3, Timestamp.class));
                review.setRating(result.getObject(4, Integer.class));
                review.setComment(result.getObject(5, String.class));
                review.setPlant(plant);
                reviews.add(review);
            }
            connection.close();
            
        } catch (SQLException e) { 
            e.printStackTrace();
        }
        return reviews;
    }
    
    /**
     * Add review to the DB.
     * 
     * @param review 
     */
    public void addReview(Review review) {
        // Connect to DB
        try (Connection connection = dataSource.getConnection()) {
            // Create INSERT statement
            String sql = "INSERT INTO reviews "
                       + "(user_name, plant_id, time_posted, rating, comment) "
                       + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            // Set statement's variables
            statement.setString(1, review.getUserName());
            statement.setInt(2, review.getPlant().getId());
            statement.setTimestamp(3, review.getTimePosted());
            statement.setInt(4, review.getRating());
            statement.setString(5, review.getComment());
            
            // Execute query and close connection
            statement.executeUpdate();
            connection.close();
            
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
    /**
     * Update a review from DB.
     * 
     * @param review 
     */
    public void updateReview(Review review) {
        // Connecto to DB
        try (Connection connection = dataSource.getConnection()) {
            // Create UPDATE statement
            String sql = "UPDATE reviews "
                       + "SET rating = ?, comment = ? "
                       + "WHERE user_name = ? AND plant_id = ? AND time_posted = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            // Fill statement's variables
            statement.setInt(1, review.getRating());
            statement.setString(2, review.getComment());
            statement.setString(3, review.getUserName());
            statement.setInt(4, review.getPlant().getId());
            statement.setTimestamp(5, review.getTimePosted());
            
            // Execute query and close connection
            statement.executeUpdate();
            connection.close();
            
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
    /**
     * Remove review from DB.
     * 
     * @param review 
     */
    public void removeReview(Review review) {
        // Connect to DB
        try (Connection connection = dataSource.getConnection()) {
            // Create DB statement
            String sql = "DELETE FROM reviews "
                       + "WHERE user_name = ? AND plant_id = ? AND time_posted = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            // Fill statement's variables
            statement.setString(1, review.getUserName());
            statement.setInt(2, review.getPlant().getId());
            statement.setTimestamp(3, review.getTimePosted());
            
            // Execute query and close connection
            statement.executeUpdate();
            connection.close();
            
        } catch (SQLException e) { 
            e.printStackTrace();
        }
    }
    
    /**
     * Remove group of reviews from DB.
     * 
     * @param reviews 
     */
    public void removeReviews(ArrayList<Review> reviews) {
        for (Review r: reviews) {
            removeReview(r);
        }
    }
}