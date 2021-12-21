package com.milk.restfilelogger.exception;

import java.util.function.Supplier;

/**
 * @author Jack Milk
 */
public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message) {
        super(message);
    }
}
