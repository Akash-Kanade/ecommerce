package com.ecommerce.userservice.exception;

public class UserAlreadyExistException extends Exception{
    public UserAlreadyExistException(String msg)
    {
        super(msg);
    }
}
