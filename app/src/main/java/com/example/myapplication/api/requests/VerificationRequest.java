package com.example.myapplication.api.requests;

/**
 * 이메일 인증 요청을 위한 클래스
 */
public class VerificationRequest {
    private String email;

    // 생성자
    public VerificationRequest(String email) {
        this.email = email;
    }

    // getter: email 값 가져오기
    public String getEmail() {
        return email;
    }

    // setter: email 값 설정하기 (필요하다면 사용)
    public void setEmail(String email) {
        this.email = email;
    }
}
