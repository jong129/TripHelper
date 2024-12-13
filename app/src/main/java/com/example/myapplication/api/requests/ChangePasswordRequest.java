package com.example.myapplication.api.requests;

public class ChangePasswordRequest {
    private String userId;
    private String email;
    private String verificationCode;
    private String oldPassword;
    private String newPassword;

    // 생성자
    public ChangePasswordRequest(String userId, String email, String verificationCode, String oldPassword, String newPassword) {
        this.userId = userId;
        this.email = email;
        this.verificationCode = verificationCode;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    // Getter 및 Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
