/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrimePackage;

import java.io.Serializable;

/**
 * Information stored in each node of the treeTable displaying the orders.
 * Such a node can represent either an order, or one type of item purchased in
 * an order.
 * @author Ron Mosenzon
 */
public class TreeNodeInfo implements Serializable {

    /* Information that applies to all nodes. */
    private String name;
    
    /* Information that applies to nodes representing one purchase in an order. */
    private String amount;
    private String price;
    
    /* Information that applies to nodes representing an order. */
    private String total;
    private String status;
    
    /**
     * Constructor.
     * For nodes that represent an order, the fields 'amount' and 'price' should
     * be given null values.
     * For nodes that represent a single purchase in an order, the fields
     * 'total' and 'status' should be given null values.
     * @param name The name of the order, or the name of item purchased
     * @param amount The amount of the item that was purchased (example: "2 packages")
     * @param price Price of the purchase (example: the price of packages of this item)
     * @param total The total price of the order
     * @param status The status of the order. Either 'Not sent yet', 'On the way' or 'Arrived'
     */
    public TreeNodeInfo(String name, String amount, String price, String total, String status){
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.total = total;
        this.status = status;
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
    
}
