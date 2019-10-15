package com.example.tapp.library;


public enum Arn {

    IOS("arn:aws:sns:us-east-1:794074012758:app/APNS_SANDBOX/tapp_ios_dev"), ANDROID("");
    private String action;

    public String getAction() {
        return this.action;
    }

    private Arn(String action) {
        this.action = action;
    }
}
