package com.jacek.nutriweek.common.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String msg) { super(msg); }
}
