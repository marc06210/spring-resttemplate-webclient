package com.mgu.oauthclient.controller;

public class MyCustomServerException extends Throwable {

    private static final long serialVersionUID = -1138231306740243450L;

    public MyCustomServerException() {
        super(">>> error no data");
    }
    
    public MyCustomServerException(String message) {
        super(">>> " + message);
    }
}
