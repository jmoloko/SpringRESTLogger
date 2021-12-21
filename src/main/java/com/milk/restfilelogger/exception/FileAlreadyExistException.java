package com.milk.restfilelogger.exception;

/**
 * @author Jack Milk
 */
public class FileAlreadyExistException extends Exception{
    public FileAlreadyExistException(String message) {
        super(message);
    }
}
