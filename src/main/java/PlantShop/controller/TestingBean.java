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
import PlantShop.model.UserModel;
import PlantShop.view.MessagesView;
import java.io.Serializable;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author Ron Mosenzon
 */
@Named(value = "testingBean")
@ViewScoped
public class TestingBean implements Serializable {
    
    @Inject
    private UserModel userModel;
    
    @Inject
    private PlantsDao plantsDao;
    
    @Inject
    private OrdersDao ordersDao;
    
    @Inject
    private MessagesView messagesView;
    
    /**
     * Creates a new instance of TestingBean
     */
    public TestingBean() {
    }
    
    
    public void addTestOrders() {
        if(!userModel.isLoggedIn()){
            messagesView.displayErrorMessage("not logged in!");
            return;
        }
        
        ArrayList<Plant> allPlants;
        try{
            allPlants = plantsDao.getPlants();
        } catch(DaoException e){
            e.printStackTrace();
            messagesView.displayErrorMessage("failed to retrieve plants at TestingBean");
            return;
        }
        
        if(allPlants.size() < 2) {
            messagesView.displayErrorMessage("too little plants in DB to build test order (need at least 2)");
            return;
        }
        
        ArrayList<Order.PlantInOrder> plants1 = new ArrayList<>();
        plants1.add(new Order.PlantInOrder(allPlants.get(0), 2));
        plants1.add(new Order.PlantInOrder(allPlants.get(1), 3));
        
        addTestOrder(plants1);
    }
    
    
    private void addTestOrder(ArrayList<Order.PlantInOrder> plants) {
        try{
            ordersDao.AddNewOrder(userModel.getUser().getUsername()
                    , Timestamp.from(Instant.now()), "Not sent yet", plants);
        } catch(DaoException e) {
            String message = e.getMessage();
            if(message == null)
                message = "";
            messagesView.displayErrorMessage("Dao exception: " + message);
        }
    }
    
    
}