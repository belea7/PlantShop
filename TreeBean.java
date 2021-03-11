import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * Bean used to fill up the treeTable that displays the orders in the
 * current_orders page.
 * @author Ron Mosenzon
 */
@Named(value = "treeBean")
@ViewScoped
public class TreeBean implements Serializable {
    
    private TreeNode root;
    /**
     * this field is for testing and debugging purposes only.
     */
    private String testVal = "not clicked";
    
    
    public TreeBean() {
        
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
        root = new DefaultTreeNode(TreeNodeInfo.createRootInfo(), null);
        
        /* Some example orders */
        TreeNode node1 = new DefaultTreeNode(TreeNodeInfo.createOrderInfo("Order 1","9$","Arrived"), root);
        TreeNode node2 = new DefaultTreeNode(TreeNodeInfo.createOrderInfo("Order 2","0$","Not sent yet"), root);
        TreeNode node3 = new DefaultTreeNode(TreeNodeInfo.createOrderInfo("Order 3","5$","On the way"), root);
        
        /* Some example purchases in the above orders */
        new DefaultTreeNode(TreeNodeInfo.createPurchaseInfo("plant1","3 packages","6$"), node1);
        new DefaultTreeNode(TreeNodeInfo.createPurchaseInfo("plant2","1 package","4$"), node1);
        new DefaultTreeNode(TreeNodeInfo.createPurchaseInfo("plant3","1 package","5$"), node3);
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
        root = new DefaultTreeNode(TreeNodeInfo.createRootInfo(), null);
        
        /* Some example users */
        TreeNode userNode1 = new DefaultTreeNode(TreeNodeInfo.createUserInfo("User 1"), root);
        TreeNode userNode2 = new DefaultTreeNode(TreeNodeInfo.createUserInfo("User 2"), root);
        
        /* Some example orders */
        TreeNode orderNode1 = new DefaultTreeNode(TreeNodeInfo.createOrderInfo("Order 1","9$","Arrived"), userNode1);
        TreeNode orderNode2 = new DefaultTreeNode(TreeNodeInfo.createOrderInfo("Order 2","0$","Not sent yet"), userNode1);
        TreeNode orderNode3 = new DefaultTreeNode(TreeNodeInfo.createOrderInfo("Order 3","5$","On the way"), userNode1);
        TreeNode orderNode4 = new DefaultTreeNode(TreeNodeInfo.createOrderInfo("MyOrder","7$","Arrived"), userNode2);
        TreeNode orderNode5 = new DefaultTreeNode(TreeNodeInfo.createOrderInfo("Order 2","0$","Arrived"), userNode2);
        
        /* Some example purchases in the above orders */
        new DefaultTreeNode(TreeNodeInfo.createPurchaseInfo("plant1","3 packages","6$"), orderNode1);
        new DefaultTreeNode(TreeNodeInfo.createPurchaseInfo("plant2","1 package","4$"), orderNode3);
        new DefaultTreeNode(TreeNodeInfo.createPurchaseInfo("plant3","1 package","5$"), orderNode3);
        new DefaultTreeNode(TreeNodeInfo.createPurchaseInfo("plant1","1 package","2$"), orderNode5);
        new DefaultTreeNode(TreeNodeInfo.createPurchaseInfo("plant2","1 package","4$"), orderNode5);
        new DefaultTreeNode(TreeNodeInfo.createPurchaseInfo("plant3","1 package","5$"), orderNode5);
    }
    
    /**
     * Called by the treeTable component to get the root of the tree.
     */
    public TreeNode getRoot() {
        if(root == null)
            System.err.println("getRoot called while root is null!"
                    + "(probably forgot to initiate root for this page)");
        return root;
        /* Debug note: because of how treeTable works, this MUST always return
        the same node object. */
    }
    
    /**
     * Method for testing and debugging purposes only.
     */
    public void buttonAction(ActionEvent event){
        testVal = "Clicked!!!";
//        TreeNode node4 = new DefaultTreeNode(TreeNodeInfo.createOrderInfo("Order 4","9$","Arrived"), root);
//        System.out.println("before name: " + ((TreeNodeInfo)(root.getChildren().get(2).getData())).getName()
//                            + "\nbefore status: " + ((TreeNodeInfo)(root.getChildren().get(2).getData())).getStatus());
//        ((TreeNodeInfo)(root.getChildren().get(2).getData())).setStatus("TestArrived");
//        System.out.println("after name: " + ((TreeNodeInfo)(root.getChildren().get(2).getData())).getName()
//                            + "\nafter status: " + ((TreeNodeInfo)(root.getChildren().get(2).getData())).getStatus());
    }
    
    /**
     * Method for testing and debugging purposes only.
     */
    public String getTestVal(){
        return testVal;
    }
    
}
