/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author MariHaugen
 */
public class GenericException extends Exception {
    
    public GenericException (String message) {
        super("Internal Server Problem. We are sorry for the inconvenience");
    }
}
