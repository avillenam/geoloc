package com.uned.geoloc.Model;

public class Message {
    private String response;

    public Message(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "" + response;
    }
}
