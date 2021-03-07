import java.io.Serializable;

/**
 * Information stored in each node of the treeTable displaying the orders.
 * Such a node can represent either an order, or one type of item purchased in
 * an order.
 * @author Ron Mosenzon
 */
public class TreeNodeInfo implements Serializable {
    
    /**
     * An enum representing which type of node this TreeNodeInfo is for.
     * Either the root node, a node representing a user in the all_orders page,
     * a node representing an order, or a node representing the purchase of one
     * type of item inside an order.
     */
    public enum NodeType {
        ROOT,
        USER,
        ORDER,
        PURCHASE
    }
    
    /* Information that applies to all nodes. */
    private final NodeType type;
    private String name;
    
    /* Information that applies to nodes representing one purchase in an order. */
    private String amount;
    private String price;
    
    /* Information that applies to nodes representing an order. */
    private String total;
    private String status;
    
    /**
     * Private constructor (used by the static create______Info methods).
     * For nodes that represent an order, the fields 'amount' and 'price' should
     * be given null values.
     * For nodes that represent a single purchase in an order, the fields
     * 'total' and 'status' should be given null values.
     * For the root node and for nodes that represent a user, only the fields
     * 'type' and 'name' should be given non-null values.
     * @param type the type of of node that this TreeNodeInfo is for (see
     * documentation for the enum NodeType)
     * @param name The name of the order, or the name of item purchased
     * @param amount The amount of the item that was purchased (example: "2 packages")
     * @param price Price of the purchase (example: the price of packages of this item)
     * @param total The total price of the order
     * @param status The status of the order. Either 'Not sent yet', 'On the way' or 'Arrived'
     */
    private TreeNodeInfo(NodeType type, String name, String amount, String price, String total,
            String status){
        this.type = type;
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.total = total;
        this.status = status;
    }
    
    /**
     * Creates and returns the TreeNodeInfo object for the root node.
     * @return the TreeNodeInfo object for the root node.
     */
    public static TreeNodeInfo createRootInfo(){
        return new TreeNodeInfo(NodeType.ROOT, "root", null, null, null, null);
    }
    
    /**
     * Creates and returns a TreeNodeInfo object representing an order.
     * @param name the name of the order
     * @param total the total price of the order
     * @param status The status of the order. Either 'Not sent yet', 'On the way' or 'Arrived'
     * @return a TreeNodeInfo object representing an order.
     */
    public static TreeNodeInfo createOrderInfo(String name, String total,
            String status) {
        return new TreeNodeInfo(NodeType.ORDER, name, null, null, total, status);
    }
    
    /**
     * Creates and returns a TreeNodeInfo object representing a purchase of one
     * type of item in an order.
     * @param name the name of the item
     * @param amount the amount of the item that was bought
     * @param price the total price of this type of item in this order
     * @return a TreeNodeInfo object representing a purchase of one type of
     * item in an order
     */
    public static TreeNodeInfo createPurchaseInfo(String name, String amount, String price) {
        return new TreeNodeInfo(NodeType.PURCHASE, name, amount, price, null, null);
    }
    
    /**
     * Creates and returns a TreeNodeInfo object representing a user in the
     * all_orders page.
     * @param name the name of the user
     * @return a TreeNodeInfo object representing a user in the all_orders page
     */
    public static TreeNodeInfo createUserInfo(String name) {
        return new TreeNodeInfo(NodeType.USER, name, null, null, null, null);
    }
    
    public String getName(){
        return name;
    }
    
    public String getAmount(){
        return amount;
    }
    
    public String getPrice(){
        return price;
    }
    
    public String getTotal(){
        return total;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    
    public String getStatus(){
        return status;
    }
    
    /**
     * @return true if this object represents the information of an order, false
     * otherwise.
     */
    public boolean isOrder() {
        return (this.type == NodeType.ORDER);
    }
    
    /**
     * @return true if this object represents the information of the purchase
     * of a specific type of item inside an order, false otherwise.
     */
    public boolean isPurchase() {
        return (this.type == NodeType.PURCHASE);
    }
    
}

