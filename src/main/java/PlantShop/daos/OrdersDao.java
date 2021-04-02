package PlantShop.daos;

import PlantShop.entities.Order;
import PlantShop.entities.Order.PlantInOrder;
import PlantShop.entities.Plant;
import PlantShop.exceptions.DaoException;
import PlantShop.exceptions.DatabaseInconsistencyException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
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
 * A class that abstracts database queries about Order objects.
 * @author Ron Mosenzon
 */
@Named(value = "ordersDao")
@Dependent
public class OrdersDao implements Serializable {
    
    // allow the server to inject the DataSource
    @Resource(lookup="java:global/jdbc/PlantShop")
    DataSource dataSource;
    
    @Inject
    private PlantsDao plantsDao;
    
    
    /**
     * Creates a new instance of OrdersDao
     */
    public OrdersDao() {
    }
    
    
    /**
     * Add a new order to the database.
     * @param username the user that this order belongs to.
     * @param timeOrdered a timestamp representing the time at which the order was ordered.
     * @param status the status of the order. (either 'Not sent yet', 'On the way' or 'Arrived')
     * @param plants the plants in the order.
     * @return An order object representing the order that was added to the
     * database as a result of this method.
     * @throws DaoException if there was a problem with database access.
     */
    public Order AddNewOrder(String username, Timestamp timeOrdered
            , String status, ArrayList<Order.PlantInOrder> plants) throws DaoException {
        
        long orderId; // will contain the id of the new order once that id is received from the DB
        
        try (Connection connection = dataSource.getConnection()) {
            // insert order in orders table
            String orderSql = "INSERT INTO orders(user_name, time_ordered, status)"
                    + " VALUES (?, ?, ?)";
            PreparedStatement orderStatement = connection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderStatement.setString(1, username);
            orderStatement.setTimestamp(2, timeOrdered);
            orderStatement.setString(3, status);
            orderStatement.execute();
            
            // get id of inserted order
            ResultSet generatedKey = orderStatement.getGeneratedKeys();
            if(generatedKey == null || !generatedKey.next()){
                String error = "no generated key on addOrder";
                if(generatedKey == null)
                    error += " (generatedKey is null)";
                try {
                    String sqlFix = "DELETE FROM orders"
                            + " WHERE user_name = ? and time_ordered = ? and status = ?";
                    PreparedStatement statementFix = connection.prepareStatement(sqlFix);
                    statementFix.setString(1, username);
                    statementFix.setTimestamp(2, timeOrdered);
                    statementFix.setString(3, status);
                    statementFix.execute();
                } catch(SQLException e) {
                    e.printStackTrace();
                    error += "\nfailed to undo order insertion, need to manually delete new order!";
                }
                throw new DaoException(error);
            }
            orderId = generatedKey.getLong(1);
            
            // insert plants into plants_in_orders table
            if(!plants.isEmpty()) {
                
                // build query
                String plantSql = "INSERT INTO plants_in_orders(order_id, plant_id, amount)"
                        + " VALUES (?, ?, ?)";
                for(int i = 0; i < plants.size() - 1; i++)
                    plantSql += ",(?, ?, ?)";
                
                PreparedStatement plantStatement = connection.prepareStatement(plantSql);
                
                // insert paremeters
                int nextParameter = 1;
                for(PlantInOrder plant : plants) {
                    plantStatement.setLong(nextParameter, orderId);
                    nextParameter++;
                    plantStatement.setLong(nextParameter, plant.getPlant().getId());
                    nextParameter++;
                    plantStatement.setInt(nextParameter, plant.getAmount());
                    nextParameter++;
                }
                
                plantStatement.execute();
            }
            
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException();
        }
        
        return new Order(orderId, timeOrdered, status, plants);
    }
    
    
    /**
     * method for debugging purposes only.
     * prints out all the order IDs in the database.
     * @param connection a connection to use to the database.
     */
    private void printOrders(Connection connection) {
        try{
            String sql = "SELECT * FROM orders";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()) {
                System.out.println("Order " + result.getLong("order_id"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Updates the status of an order in the database.
     * @param orderID the ID of the order.
     * @param newStatus the new status that the order will have after the update.
     * @throws DaoException if there was a problem with database access.
     */
    public void updateOrderStatus(long orderID, String newStatus) throws DaoException {
        
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE orders"
                    + " SET status = ?"
                    + " WHERE order_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newStatus);
            statement.setLong(2, orderID);
            statement.execute();
            
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException();
        }
        
    }
    
    
    /**
     * Retrieves all the orders in the DB that belong to the user with the specified orders.
     * @param username the username of the user
     * @return a UserOrders object with all the orders of this user
     * @throws DaoException if there was a problem with database access.
     */
    public UserOrders getUserOrders(String username) throws DaoException {
        ArrayList<UserOrders> list = getOrders("WHERE user_name='" + username + "'");
        if(list.isEmpty()){
            return new UserOrders(username);
        } else {
            return list.get(0);
        }
    }
    
    
    /**
     * Retrieves the orders of all users from the database.
     * @return all orders in the DB, grouped together based on the user that they belong to.
     * @throws DaoException if there was a problem with database access.
     */
    public ArrayList<UserOrders> getAllUserOrders() throws DaoException {
        return getOrders("");
    }
    
    
    /**
     * Retrieves from the database all orders that satisfy the specified constraint.
     * @param constraint a string specifying a constraint in sql. (i.e. 'WHERE ID=1')
     * @return the orders in the database that satisfy the constraint,
     * grouped together based on the user that they belong to.
     * @throws DaoException if there was a problem with database access.
     */
    private ArrayList<UserOrders> getOrders(String constraint) throws DaoException {
        
        try (Connection connection = dataSource.getConnection()) {
            
            if(constraint.compareTo("") != 0)
                constraint += " ";
            
            // Execute get statement for contents of orders (from plants_in_orders)
            String contentsSql = "SELECT * FROM orders NATURAL JOIN plants_in_orders "
                    + constraint + "ORDER BY user_name ASC, order_id ASC";
            Statement contentsStatement = connection.createStatement();
            ResultSet contentsResult = contentsStatement.executeQuery(contentsSql);
            
            // build contents of orders
            ArrayList<OrderContents> contents = parseOrderContents(contentsResult);
            
            // Execute get statement for orders
            String ordersSql = "SELECT * FROM orders " + constraint
                    + "ORDER BY user_name ASC, order_id ASC";
            Statement ordersStatement = connection.createStatement();
            ResultSet ordersResult = ordersStatement.executeQuery(ordersSql);
            
            // build orders
            ArrayList<UserOrders> orders = parseOrders(ordersResult);
            
            // close connection
            connection.close();
            
            // put contents into orders
            MergeContentsIntoOrders(orders, contents);
                    
            return orders;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException();
        }
        
    }
    
    
    /**
     * Organizes the result of a query that retrieves the content of
     * orders into PlantInOrder object, grouped together in OrderContents objects.
     * This method has large overhead, so don't call it many times.
     * @param queryResult the ResultSet returned by query for retrieving
     * lines from the plants_in_orders table in the database.
     * Lines that belong to the same order must be arranged consecutively in the
     * ResultSet so that they can be correctly grouped together into OrderContents objects.
     * @return a list of OrderContents objects, each one containing the ID of
     * an order, and a list of PlantInOrder objects that belong to that order.
     * The list is sorted in the same order of ID as the ResultSet.
     * @throws SQLException if there was a problem with retrieving items from queryResult.
     * @throws DaoException if there was a problem with retrieving plants from the DB.
     */
    private ArrayList<OrderContents> parseOrderContents(ResultSet queryResult)
            throws SQLException, DaoException {
        
        ArrayList<OrderContents> orderList = new ArrayList<>();
        long newOrderID;
        Plant newPlant;
        
        ArrayList<Plant> allPlants = plantsDao.getPlants();
        HashMap<Long,Plant> allPlantsMap = new HashMap<>();
        for(Plant plant : allPlants)
            allPlantsMap.put(plant.getId(), plant);
        
        
        OrderContents currentOrder = new OrderContents(-1); // start with dummy order
        while(queryResult.next()) {
            
            newOrderID = queryResult.getObject("order_id", Long.class);
            if(currentOrder.getID() != newOrderID) {
                currentOrder = new OrderContents(newOrderID);
                orderList.add(currentOrder);
            }
            
            newPlant = allPlantsMap.get(queryResult.getObject("plant_id", Long.class));
            currentOrder.addPlant(newPlant, queryResult.getObject("amount", Integer.class));
        }
        
        return orderList;
    }
    
    
    /**
     * Converts the result of a query that retrieves orders into empty Orders
     * objects, grouped together in UserOrders objects.
     * @param queryResult the ResultSet returned by query for retrieving
     * lines from the orders table in the database.
     * Lines that belong to the same user  must be arranged consecutively in the
     * ResultSet so that they can be correctly grouped together into UserOrders objects.
     * @return A list of UserOrders objects containing Order objects
     * constructed from the information in the ResultSet. These Order objects
     * do not contain any plants.
     * The Orders are ordered in the same way as the lines in the ResultSet.
     * @throws SQLException if there was a problem with retrieving items from queryResult.
     */
    private ArrayList<UserOrders> parseOrders(ResultSet queryResult) throws SQLException {
        
        ArrayList<UserOrders> userList = new ArrayList<>();
        String newUsername;
        
        
        UserOrders currentUser = new UserOrders(""); // start with dummy user
        while(queryResult.next()) {
            
            newUsername = queryResult.getObject("user_name", String.class);
            if(currentUser.getUserName().compareTo(newUsername) != 0) {
                currentUser = new UserOrders(newUsername);
                userList.add(currentUser);
            }
            
            currentUser.addOrder(new Order(
                    queryResult.getObject("order_id", Long.class),
                    queryResult.getObject("time_ordered", Timestamp.class),
                    queryResult.getObject("status", String.class) ));
            
        }
        
        return userList;
    }
    
    
    /**
     * Fills the orders in a list of UserOrders objects with contents from a
     * list of OrderContents objects. The list of OrderContents and the
     * concatenation of order lists stored in the UserOrders objects must be of
     * the same length, and must agree on the ID of the order in each position.
     * @param orders A list of UserOrders objects with empty orders, as
     * returned by the parseOrders method.
     * @param contents A list of OrderContents objects, as returned by the
     * parseOrderContents method.
     * @throws DatabaseInconsistencyException if the number of orders is
     * different than the number of OrderContents objects, or if the order of
     * order IDs is different in the two lists.
     */
    private void MergeContentsIntoOrders(ArrayList<UserOrders> orders, ArrayList<OrderContents> contents)
            throws DatabaseInconsistencyException {
        
        OrderContents orderContents;
        
        ListIterator<OrderContents> contentsIterator = contents.listIterator();
        
        for(UserOrders userOrders : orders) {
            for(Order order : userOrders.getOrders()) {
                
                if( !contentsIterator.hasNext() )
                    throw new DatabaseInconsistencyException(
                            "list of orders has more orders than list of order contents.");
                
                orderContents = contentsIterator.next();

                if(order.getID() != orderContents.getID())
                    throw new DatabaseInconsistencyException(
                            "list of orders and list of order contents do not agree on ID. " + order.getID() + " vs. " + orderContents.getID() + ".");

                order.setPlants(orderContents.getPlants());
            }
        }
        
        if(contentsIterator.hasNext())
            throw new DatabaseInconsistencyException(
                    "list of orders has less orders than list of order contents.");
    }
    
    
    /**
     * A class for grouping together all the orders in the DB that belong to a specific user.
     * @author Ron Mosenzon
     */
    public static class UserOrders implements Serializable {
        
        private final String username;
        private ArrayList<Order> orderList;
        
        /**
         * Create a UserOrders object for grouping together the orders of a user.
         * @param username the name of the user.
         */
        public UserOrders(String username) {
            this.username = username;
            orderList = new ArrayList<Order>();
        }
        
        
        /**
         * Add an order to this UserOrders object.
         * @param order the order to be added.
         */
        void addOrder(Order order) {
            orderList.add(order);
        } 
        
        
        /**
         * @return the username of the user whose orders are stored in this UserOrders object.
         */
        public String getUserName() {
            return username;
        }
        
        /**
         * @return an ArrayList of the orders stored in this UserOrders object.
         */
        public ArrayList<Order> getOrders() {
            return new ArrayList<Order>(orderList);
        }
        
    }
    
    
    /**
     * A class representing the contents of an order in the database.
     * @author Ron Mosenzon
     */
    private static class OrderContents {
        
        private final long orderID;
        ArrayList<PlantInOrder> plants;
        
        /**
         * Create an OrderContents object representing the contents
         * of the order with the specified ID.
         * @param ID the ID of the order whose contents are represented by this object.
         */
        public OrderContents(long ID) {
            this.orderID = ID;
            plants = new ArrayList<PlantInOrder>();
        }
        
        /**
         * Add a new plant to this OrderContents.
         * @param plant the type of plant to add.
         * @param amount the amount of this plant that should be in this orderContent.
         */
        public void addPlant(Plant plant, int amount) {
            plants.add(new Order.PlantInOrder(plant, amount));
        }
        
        
        /**
         * @return the contents of this order, in a form accepted by the Order constructor.
         */
        public ArrayList<PlantInOrder> getPlants() {
            return plants;
        }
        
        
        /**
         * @return the ID of the order whose contents are represented by this object.
         */
        public long getID() {
            return orderID;
        }
        
    }
    
}