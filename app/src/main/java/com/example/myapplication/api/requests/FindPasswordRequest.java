package com.example.myapplication.api.requests;

public class FindPasswordRequest {
    private final String userId;
    private final String email;
    private final String verificationCode;
    private final String newPassword;

    public FindPasswordRequest(String userId, String email, String verificationCode, String newPassword) {
        this.userId = userId;
        this.email = email;
        this.verificationCode = verificationCode;
        this.newPassword = newPassword;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
