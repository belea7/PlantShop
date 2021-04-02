/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlantShop.util;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * A class representing a price
 * @author Ron Mosenzon
 */
public class Price implements Serializable {
    
    private final double priceInILS;
    
    
    /**
     * private constructor.
     * @param priceInILS the price, in ILS.
     */
    private Price(double priceInILS) {
        this.priceInILS = priceInILS;
    }
    
    
    /**
     * Copy constructor.
     * @param price the price this price will be a copy of.
     */
    public Price(Price price) {
        this.priceInILS = price.priceInILS;
    }
    
    
    /**
     * Constructs a price object representing the sum of two prices.
     * @param p1 a price.
     * @param p2 a price.
     * @return a price object representing the sum of the two prices.
     */
    public static Price sum(Price p1, Price p2) {
        return new Price(p1.priceInILS + p2.priceInILS);
    }
    
    
    /**
     * Constructs a price object whose price is the product of some price and a given constant factor.
     * @param price the original price
     * @param multiplier the constant factor
     * @return a price object whose price is the product of the original price and the constant factor.
     */
    public static Price multiplyPrice(Price price, double multiplier) {
        return new Price(price.priceInILS * multiplier);
    }
    
    
    /**
     * Constructs a price object representing a price of 0.
     * @return a price object representing a price of 0.
     */
    public static Price getZeroPrice() {
        return new Price(0);
    }
    
    
    /**
     * Creates a new Price object from the price format received from the database (a double).
     * @param dbFormat the price, in the format received from the database.
     * @return a Price object representing this price.
     */
    public static Price fromDBFormat(double dbFormat) {
        return new Price(dbFormat);
    }
    
    
    /**
     * Converts this price to the format which will be used to store it in the database (a double).
     * @return this price, in the format used by the database.
     */
    public double toDBFormat() {
        return priceInILS;
    }
    
    
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return df.format(priceInILS) + " ILS";
    }
    
    
}