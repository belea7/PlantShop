/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlantShop.exceptions;

/**
 * An exception thrown when an inconsistency in the database is detected.
 * @author Ron Mosenzon
 */
public class DatabaseInconsistencyException extends RuntimeException {
    
    public DatabaseInconsistencyException() {
        super();
    }
    
    public DatabaseInconsistencyException(String message) {
        super(message);
    }
    
}
