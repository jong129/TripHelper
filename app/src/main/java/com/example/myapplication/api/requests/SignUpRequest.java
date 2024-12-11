package com.example.myapplication.api.requests;

public class SignUpRequest {
    private String email;
    private String verificationCode;
    private String id;
    private String password;

    public SignUpRequest(String email, String verificationCode, String id, String password) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.id = id;
        this.password = password;
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
