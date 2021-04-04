package PlantShop.controller;

import PlantShop.view.OrdersViewBean;
import PlantShop.daos.OrdersDao;
import PlantShop.entities.Order;
import PlantShop.exceptions.DaoException;
import PlantShop.util.ErrorDisplay;

/**
 * A node representing an order in the TreeTable, that has an editable status field.
 * @author Ron Mosenzon
 */
public class EditableOrderTreeNode extends OrdersViewBean.DefaultTreeNodeInfo {
    
    private final Order orderToUpdate;
    private final OrdersDao ordersDao;
    private final ErrorDisplay errorDisplay;
    private String status; // either 'Not sent yet', 'On the way' or 'Arrived'
    
    /**
     * @param orderToUpdate the order entity whose status will be updated
     * when the user updates the status of this node.
     * @param name The name of the order, or the name of item purchased
     * @param total The total price of the order
     * @param status The status of the order. Either 'Not sent yet', 'On the way' or 'Arrived'
     * @param dao A reference to the dao, so the node can update the status of orders in the database.
     * @param errorDisplay an object that this node will use to display error messages.
     */
    public EditableOrderTreeNode(Order orderToUpdate, String name, String total,
            String status, OrdersDao dao, ErrorDisplay errorDisplay) {
        super(name, "", "", total, status);
        this.orderToUpdate = orderToUpdate;
        this.status = status;
        this.ordersDao = dao;
        this.errorDisplay = errorDisplay;
    }
    
    /**
     * Sets the status of this node and of the corresponding order, including updating the database.
     * @param status the new status.
     */
    @Override
    public void setStatus(String status) {
        if(status.compareTo(this.status) != 0) {
            String originalStatus = this.status;
            this.status = status;
            try {
                ordersDao.updateOrderStatus(orderToUpdate.getID(), status);
                orderToUpdate.setStatus(status);
            } catch(DaoException e) {
                this.status = originalStatus;
                errorDisplay.displayErrorMessage(
                        "Sorry, there was a problem when saving the status to the database!");
            } catch(NullPointerException e) {
                // this should never happen
                this.status = originalStatus;
                e.printStackTrace();
                errorDisplay.displayErrorMessage(
                        "Sorry, we enountered a problem! Try to refresh the page.");
            }
        }
    }
    
    /**
     * @return the status of this order, either 'Not sent yet', 'On the way' or 'Arrived'.
     */
    @Override
    public String getStatus() {
        return status;
    }
    
    @Override
    public boolean isStatusEditable() {
        return true;
    }
    
}
