package com.example.myapplication.api.requests;

public class LoginRequest {
    private String id; // 서버에서 기대하는 필드 이름에 맞춰 'id'로 변경
    private String password;

    public LoginRequest(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
