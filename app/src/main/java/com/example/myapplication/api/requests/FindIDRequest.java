package com.example.myapplication.api.requests;

public class FindIDRequest {
    private String email;           // 이메일
    private String verificationCode; // 인증번호

    // 이메일 인증번호 요청용 생성자
    public FindIDRequest(String email) {
        this.email = email;
    }

    // 아이디 찾기용 생성자
    public FindIDRequest(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }

    // Getter와 Setter 메서드
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
}
