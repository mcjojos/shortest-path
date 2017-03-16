package com.jojos.path;

/**
 * The main custom application exception defined as a runtime exception so that it doesn't have to appear everywhere
 *
 * @author gkaranikas
 */
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
        System.err.println(message);
    }
}
