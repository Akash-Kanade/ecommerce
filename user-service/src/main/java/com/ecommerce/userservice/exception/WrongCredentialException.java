package com.ecommerce.userservice.exception;

public class WrongCredentialException extends Exception{
    public WrongCredentialException(String msg)
    {
        super(msg);
    }
}
