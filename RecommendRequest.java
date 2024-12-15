package com.example.myapplication.api.requests;

@SuppressWarnings("unused") // 사용되지 않는 메서드에 대한 경고 무시
public class RecommendRequest {
    private final String purpose;
    private final String activity;
    private final String accommodation;

    public RecommendRequest(String purpose, String activity, String accommodation) {
        this.purpose = purpose;
        this.activity = activity;
        this.accommodation = accommodation;
    }

    public String getPurpose() { return purpose; }
    public String getActivity() { return activity; }
    public String getAccommodation() { return accommodation; }
}
