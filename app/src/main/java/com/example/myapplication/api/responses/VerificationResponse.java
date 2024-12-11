package com.example.myapplication.api.responses;

public class VerificationResponse {
    private String message;  // 서버에서 반환하는 메시지
    private String code;     // 인증번호 (Optional)

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
