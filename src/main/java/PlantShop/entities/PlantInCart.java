/*
 * Plant in a shopping cart entity.
 */
package PlantShop.entities;

/**
 * A class representing a plant in a user's shopping cart.
 * 
 * The class contains a pointer to the Plant entity and the amount of plants
 * from this type in the user's shopping cart.
 * 
 * @author leagi
 */
public class PlantInCart {
        private Plant plant;    // Plant entity
        private int amount;     // number of plants from this type in cart
        
        /**
         * Constructor for PlantInCart entity.
         * 
         * @param plant
         * @param amount 
         */
        public PlantInCart(Plant plant, int amount) {
            if (amount > 0) {
                this.plant = plant;
                this.amount = amount;
            }
        }
        
        /**
         * Increases the amount of the plants in cart by 1.
         */
        public void increaseAmount() {
            this.amount += 1;
        }
        
        /**
         * Did the number of items in cart reach the number of items in stock.
         * 
         * @return reached or not
         */
        public boolean reachMaxAmount() {
            return this.amount == this.plant.getNumberOfItems();
        }
        
        /**
         * Decreases the amount od the plants in cart by 1.
         * The amount cannot be less than 1.
         */
        public void decreaseAmount() {
            if (this.amount > 1)
                this.amount -= 1;
        }
        
        /**
         * Getter for plant attribute.
         * 
         * @return plant
         */
        public Plant getPlant() {
            return plant;
        }
        
        /**
         * Setter for plant attribute.
         * The plant cannot be null.
         * 
         * @param plant
         */
        public void setPlant(Plant plant) {
            if (plant != null) {
                this.plant = plant;
            }
        }
        
        /**
         * Getter for amount attribute.
         * 
         * @return amount
         */
        public int getAmount() {
            return amount;
        }
        
        /**
         * Setter for amount attribute.
         * Amount cannot be less than one.
         * 
         * @param amount 
         */
        public void setAmount(int amount) {
            if (amount > 0) {
                this.amount = amount;
            }
        }
}