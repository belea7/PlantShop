/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlantShop.view;

import java.io.Serializable;

/**
 *
 * @author leagi
 */
public abstract class AbstractDataEditorBean implements Serializable{
    
    public void displayObjectDeletionMessage(String msg) { }
    
    public void displayObjectEditMessage(String msg) { }
    
    public void displayObjectCreationMessage(String msg) {}
}
