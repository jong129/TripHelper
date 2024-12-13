package com.example.myapplication.api.requests;

/**
 * VerificationRequest 클래스: 이메일 인증 요청을 처리하기 위한 요청 객체
 */
public class VerificationRequest {
    private String userId;  // 사용자 ID
    private String email;   // 사용자 이메일

    // 두 개의 매개변수를 받는 생성자
    public VerificationRequest(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    // 한 개의 매개변수를 받는 생성자
    public VerificationRequest(String email) {
        this.email = email;
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
}
