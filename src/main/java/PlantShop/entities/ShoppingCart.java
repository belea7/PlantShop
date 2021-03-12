/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlantShop.entities;

import java.util.ArrayList; 

/**
 * Shopping cart of a user.
 * @author leagi
 */
public class ShoppingCart {
    
    private User user;
    private int id;
    private ArrayList<PlantInCart> plants;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public ArrayList<PlantInCart> getPlants() {
        return plants;
    }
    
    public void addPlant(Plant plant, int amount) {
        PlantInCart newPlant = new PlantInCart(plant, amount);
        this.plants.add(newPlant);
    }

    public void removePlant(Plant plant) {
        for (PlantInCart p : this.plants) {
            if (p.getPlant().equals(plant)) {
                this.plants.remove(p);
                return;
            }
        }
    }
}
