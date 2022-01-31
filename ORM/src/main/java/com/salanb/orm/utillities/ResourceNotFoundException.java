package com.salanb.orm.utillities;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException() {super();}
    public ResourceNotFoundException(String msg) { super(msg); }
    public ResourceNotFoundException(String msg, Throwable cause) { super(msg, cause); }
    public ResourceNotFoundException(Throwable cause) {super(cause);}
    public ResourceNotFoundException(String message, Throwable cause,
                              boolean enableSuppression,
                              boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace);}

}
