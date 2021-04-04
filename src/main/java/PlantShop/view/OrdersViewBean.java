package PlantShop.view;

import PlantShop.controller.EditableOrderTreeNode;
import PlantShop.model.UserModel;
import PlantShop.daos.OrdersDao;
import PlantShop.entities.Order;
import PlantShop.exceptions.DaoException;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * Bean used to fill up the treeTable that displays the orders in the
 * current_orders page.
 * @author Ron Mosenzon
 */
@Named(value = "ordersViewBean")
@ViewScoped
public class OrdersViewBean implements Serializable {
    
    private TreeNode root;
    
    @Inject
    private UserModel userModel;
    
    @Inject
    private OrdersDao ordersDao;
    
    @Inject
    private MessagesView messagesView;
    
    public OrdersViewBean() {
        
    }
    
    /**
     * Called when the current_orders page is loaded. Creates the tree to be sent to the
     * TreeTable component.
     * Note:
     * Each node other than the root represents either an order (in which case, it is a child of
     * the root node), or one type of item purchased in an order (in which case,
     * it is a child of the node representing that order).
     */
    public void initCurrentOrders(){
        
        OrdersDao.UserOrders orders;
        
        root = new DefaultTreeNode(DefaultTreeNodeInfo.createRootInfo(), null);
        
        try {
            orders = ordersDao.getUserOrders(userModel.getUser().getUsername());
        } catch(DaoException e) {
            messagesView.displayErrorMessage("Sorry, there was a problem with connecting to the database.");
            return;
        }
        
        // sort by time so recent orders appear on top in treeTable
        ArrayList orderList = orders.getOrders();
        orderList.sort(new Comparator<Order>()
        {
            @Override
            public int compare(Order o1, Order o2) {
                return o2.getTimeOrdered().compareTo(o1.getTimeOrdered());
            }
        });
        addOrdersAsSubtree(orderList, root);
        
    }
    
    /**
     * Called when the all_orders page is loaded. Creates the tree to be sent to the
     * TreeTable component.
     * Note:
     * Each node other than the root represents either a user (in which case, it
     * is a child of the root node), or an order (in which case, it is a child
     * of a user node), or one type of item purchased in an order (in which case,
     * it is a child of the node representing that order).
     */
    public void initAllOrders(){
        
        ArrayList<OrdersDao.UserOrders> userList;
        
        root = new DefaultTreeNode(DefaultTreeNodeInfo.createRootInfo(), null);
        
        try {
            userList = ordersDao.getAllUserOrders();
        } catch(DaoException e) {
            messagesView.displayErrorMessage("Sorry, there was a problem with connecting to the database.");
            return;
        }
        
        TreeNode userNode;
        ArrayList orderList;
        for(OrdersDao.UserOrders user : userList) {
            userNode = new DefaultTreeNode(DefaultTreeNodeInfo.createUserInfo(user.getUserName()), root);
            
            // sort by time so recent orders appear on top in treeTable
            orderList = user.getOrders();
            orderList.sort(new Comparator<Order>()
            {
                @Override
                public int compare(Order o1, Order o2) {
                    return o2.getTimeOrdered().compareTo(o1.getTimeOrdered());
                }
            });
            addOrdersAsSubtree(orderList, userNode);
        }
        
    }
    
    /**
     * Called by the treeTable component to get the root of the tree.
     */
    public TreeNode getRoot() {
        return root;
        /* Debug note: because of how treeTable works, this MUST always return
        the same node object. */
    }
    
    
    /**
     * Adds tree nodes representing the specified orders as children of the target TreeNode.
     * @param orders the orders to be added.
     * @param target the TreeNode whose sub-tree will contain the orders.
     */
    private void addOrdersAsSubtree(ArrayList<Order> orders, TreeNode target) {
        
        for(Order order: orders) {
            makeOrderSubtree(order, target);
        }
    }
    
    
    /**
     * Constructs a sub-tree representing an order,
     * and adds its root as a child of the specified parent node.
     * @param order the order that the sub-tree will represent.
     * @param parent the node that will be the parent of the root of the subtree.
     */
    private void makeOrderSubtree(Order order, TreeNode parent) {
        
        DecimalFormat df = new DecimalFormat(); // price display format
        df.setMaximumFractionDigits(2);
        
        TreeNode orderNode = new DefaultTreeNode(new EditableOrderTreeNode(order
                , "Order ordered on " + order.getTimeOrdered().toLocalDateTime().toString()
                , df.format(order.getPrice())+" ILS", order.getStatus(), ordersDao, messagesView), parent);
        
        
        for(Order.PlantInOrder plant : order.getPlants()) {
            new DefaultTreeNode(DefaultTreeNodeInfo.createPlantInfo(
                    plant.getPlant().getName(), ""+plant.getAmount(), df.format(plant.getTotalPrice())+" ILS"), orderNode);
        }
        
    }
    
    
    /**
     * Information stored in a node of the treeTable displaying the orders.
     * @author Ron Mosenzon
     */
    public static interface TreeNodeInfo extends Serializable {
        
        /**
         * @return the text that should be displayed in the 'name' column.
         */
        public String getName();
        
        /**
         * @return the text that should be displayed in the 'amount' column.
         */
        public String getAmount();
        
        /**
         * @return the text that should be displayed in the 'price' column.
         */
        public String getPrice();
        
        /**
         * @return the text that should be displayed in the 'total' column.
         */
        public String getTotal();
        
        /**
         * The method called when an admin user sets the status of this node in the all_orders page.
         * Can be given an empty implementation except in nodes whose isStatusEditable method may return true.
         * @param status the status that the user assigned to this node.
         */
        public void setStatus(String status);
        
        /**
         * @return for a node whose isStatusEditable method returns true,
         * this method needs to return the current status
         * (either 'Not sent yet', 'On the way' or 'Arrived').
         * For nodes whose isStatusEditable method returns false,
         * this is the text that should be displayed in the 'status' column.
         */
        public String getStatus();

        /**
         * @return true if this nodes status should be editable in the all_orders page.
         */
        public boolean isStatusEditable();
    }
    
    
    /**
     * Information stored in a node of the treeTable displaying the orders
     * in the orders and all_orders pages.
     * @author Ron Mosenzon
     */
    public static class DefaultTreeNodeInfo implements TreeNodeInfo {
        
        private final String name;
        private final String amount;
        private final String price;
        private final String total;
        private final String status;

        /**
         * @param name The name of the order, or the name of item purchased
         * @param amount The amount of the item that was purchased (example: "2 packages")
         * @param price Price of the purchase (example: price of item multiplied by amount of this item bought)
         * @param total The total price of the order
         * @param status The status of the order. Either 'Not sent yet', 'On the way' or 'Arrived'
         */
        public DefaultTreeNodeInfo(String name, String amount, String price, String total,
                String status){
            this.name = name;
            this.amount = amount;
            this.price = price;
            this.total = total;
            this.status = status;
        }

        /**
         * Creates and returns the DefaultTreeNodeInfo object for the root node.
         * @return the DefaultTreeNodeInfo object for the root node.
         */
        public static DefaultTreeNodeInfo createRootInfo(){
            return new DefaultTreeNodeInfo("root", null, null, null, null);
        }

        /**
         * Creates and returns a DefaultTreeNodeInfo object representing an order.
         * @param name the name of the order
         * @param total the total price of the order
         * @param status The status of the order. Either 'Not sent yet', 'On the way' or 'Arrived'
         * @return a DefaultTreeNodeInfo object representing an order.
         */
        public static DefaultTreeNodeInfo createOrderInfo(String name, String total,
                String status) {
            return new DefaultTreeNodeInfo(name, null, null, total, status);
        }

        /**
         * Creates and returns a DefaultTreeNodeInfo object representing one type of plant in an order.
         * @param name the name of the plant
         * @param amount the amount of the plant that was bought
         * @param price the total price of this type of plant in this order
         * @return a DefaultTreeNodeInfo object representing one type of plant in an order
         */
        public static DefaultTreeNodeInfo createPlantInfo(String name, String amount, String price) {
            return new DefaultTreeNodeInfo(name, amount, price, null, null);
        }

        /**
         * Creates and returns a DefaultTreeNodeInfo object representing a user in
         * the all_orders page.
         * @param name the name of the user
         * @return a DefaultTreeNodeInfo object representing a user in the all_orders page
         */
        public static DefaultTreeNodeInfo createUserInfo(String name) {
            return new DefaultTreeNodeInfo(name, null, null, null, null);
        }
        
        @Override
        public String getName(){
            return name;
        }
        
        @Override
        public String getAmount(){
            return amount;
        }
        
        @Override
        public String getPrice(){
            return price;
        }
        
        @Override
        public String getTotal(){
            return total;
        }
        
        @Override
        public void setStatus(String status){}
        
        @Override
        public String getStatus(){
            return status;
        }

        /**
         * @return false.
         */
        public boolean isStatusEditable() {
            return false;
        }

    }


}
