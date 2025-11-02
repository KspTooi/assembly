package com.ksptool.assembly.entity.exception;

public class AuthException extends Exception{


    public AuthException(String msg) {
        super(msg);
    }

    public AuthException(String msg, Exception e) {
        super(msg,e);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
