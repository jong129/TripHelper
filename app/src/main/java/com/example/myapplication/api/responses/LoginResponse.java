package com.example.myapplication.api.responses;

public class LoginResponse {
    private boolean success;
    private String message;
    private String token; // 서버에서 전달받는 토큰

    // Getter와 Setter 추가
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
