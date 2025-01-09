package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.requests.RecommendRequest;
import com.example.myapplication.api.responses.RecommendResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendActivity extends AppCompatActivity {

    private Spinner spinnerPurpose, spinnerActivity, spinnerAccommodation;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        // Spinner와 TextView 연결
        spinnerPurpose = findViewById(R.id.spinnerPurpose);
        spinnerActivity = findViewById(R.id.spinnerActivity);
        spinnerAccommodation = findViewById(R.id.spinnerAccommodation);
        textViewResult = findViewById(R.id.textViewResult);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);

        // Spinner에 데이터 추가
        setUpSpinners();

        // 버튼 클릭 이벤트
        buttonSubmit.setOnClickListener(v -> handleSubmission());
    }

    private void setUpSpinners() {
        // 여행 목적
        String[] purposes = {"휴식", "음식 여행", "역사 탐방", "자연 체험"};
        ArrayAdapter<String> adapterPurpose = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, purposes);
        spinnerPurpose.setAdapter(adapterPurpose);

        // 선호 활동
        String[] activities = {"등산", "쇼핑", "캠핑", "공연 관람"};
        ArrayAdapter<String> adapterActivity = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, activities);
        spinnerActivity.setAdapter(adapterActivity);

        // 숙박 스타일
        String[] accommodations = {"호텔", "캠핑", "에어비앤비"};
        ArrayAdapter<String> adapterAccommodation = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, accommodations);
        spinnerAccommodation.setAdapter(adapterAccommodation);
    }

    private void handleSubmission() {
        // Spinner 값 가져오기
        String purpose = spinnerPurpose.getSelectedItem().toString();
        String activity = spinnerActivity.getSelectedItem().toString();
        String accommodation = spinnerAccommodation.getSelectedItem().toString();

        // 서버 요청
        fetchRecommendations(purpose, activity, accommodation);
    }

    private void fetchRecommendations(String purpose, String activity, String accommodation) {
        // RecommendRequest 객체 생성
        RecommendRequest request = new RecommendRequest(purpose, activity, accommodation);

        // Retrofit을 통해 서버 요청
        RetrofitClient.getApiService().getRecommendations(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RecommendResponse> call, @NonNull Response<RecommendResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 추천 결과 표시
                    textViewResult.setText(response.body().getRecommendation());
                } else {
                    Toast.makeText(RecommendActivity.this, "추천 결과를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecommendResponse> call, @NonNull Throwable t) {
                Toast.makeText(RecommendActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
