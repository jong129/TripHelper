package com.example.myapplication.api.requests;

public class CheckIDRequest {
    private String id;

    public CheckIDRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
