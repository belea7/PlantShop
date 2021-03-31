/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlantShop.controller;

import PlantShop.daos.OrdersDao;
import PlantShop.daos.PlantsDao;
import PlantShop.entities.Order;
import PlantShop.entities.Plant;
import PlantShop.exceptions.DaoException;
import PlantShop.model.UserBean;
import PlantShop.view.AbstractFormViewBean;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Ron Mosenzon
 */
@Named(value = "testingBean")
@ViewScoped
public class TestingBean extends AbstractFormViewBean {
    
    @Inject
    private UserBean userBean;
    
    @Inject
    private PlantsDao plantsDao;
    
    @Inject
    private OrdersDao ordersDao;
    
    @Override
    public void displayFormSubmissionErrorMessage(String msg) {
        super.displayFormSubmissionErrorMessage(msg);
        PrimeFaces.current().ajax().update("form:messages");
    }
    
    /**
     * Creates a new instance of TestingBean
     */
    public TestingBean() {
    }
    
    
    public void addTestOrders() {
        if(!userBean.isLoggedIn()){
            displayErrorMessage("not logged in!");
            return;
        }
        
        ArrayList<Plant> allPlants;
        try{
            allPlants = plantsDao.getPlants();
        } catch(DaoException e){
            e.printStackTrace();
            this.displayErrorMessage("failed to retrieve plants at TestingBean");
            return;
        }
        
        if(allPlants.size() < 2) {
            this.displayErrorMessage("too little plants in DB to build test order (need at least 2)");
            return;
        }
        
        ArrayList<Order.PlantInOrder> plants1 = new ArrayList<>();
        plants1.add(new Order.PlantInOrder(allPlants.get(0), 2));
        plants1.add(new Order.PlantInOrder(allPlants.get(1), 3));
        
        addTestOrder(plants1);
    }
    
    
    private void addTestOrder(ArrayList<Order.PlantInOrder> plants) {
        try{
            ordersDao.AddNewOrder(userBean.getUser().getUsername()
                    , Timestamp.from(Instant.now()), "Not sent yet", plants);
        } catch(DaoException e) {
            String message = e.getMessage();
            if(message == null)
                message = "";
            this.displayErrorMessage("Dao exception: " + message);
        }
    }
    
    
}