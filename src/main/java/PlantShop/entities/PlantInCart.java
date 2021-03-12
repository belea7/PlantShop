/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlantShop.entities;

/**
 *
 * @author leagi
 */
public class PlantInCart {
        private Plant plant;
        private int amount;

        public PlantInCart(Plant plant, int amount) {
            this.plant = plant;
            this.amount = amount;
        }
        
        public void changeAmount(int amount) {
            this.amount = amount;
        }

        public Plant getPlant() {
            return plant;
        }

        public void setPlant(Plant plant) {
            this.plant = plant;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
}
