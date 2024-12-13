package com.example.myapplication.api.responses;

public class ChangePasswordResponse {
    private boolean success; // 요청 성공 여부
    private String message;  // 서버에서 반환하는 메시지

    // Getter 및 Setter
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
}
