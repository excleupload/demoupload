package com.example.tapp.common.notification.mail.builder;

public class Attachment {

    private String name;
    private String path;

    public Attachment(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

}