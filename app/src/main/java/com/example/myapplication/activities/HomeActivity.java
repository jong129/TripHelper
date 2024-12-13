package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // View 연결
        // TODO: 나중에 사용
    /*
    Button buttonPlanTrip = findViewById(R.id.buttonPlanTrip);
    Button buttonRecommendTrip = findViewById(R.id.buttonRecommendTrip);
    Button buttonExploreNearby = findViewById(R.id.buttonExploreNearby);
    */
        Button buttonManageAccount = findViewById(R.id.buttonManageAccount);

        // 여행 계획 버튼 클릭 시
    /*
    buttonPlanTrip.setOnClickListener(v -> {
        Intent intent = new Intent(HomeActivity.this, PlanTripActivity.class);
        startActivity(intent);
    });

    // 여행 추천 버튼 클릭 시
    buttonRecommendTrip.setOnClickListener(v -> {
        Intent intent = new Intent(HomeActivity.this, RecommendTripActivity.class);
        startActivity(intent);
    });

    // 주변 돌아보기 버튼 클릭 시
    buttonExploreNearby.setOnClickListener(v -> {
        Intent intent = new Intent(HomeActivity.this, ExploreNearbyActivity.class);
        startActivity(intent);
    });
    */

        // 계정 관리 버튼 클릭 시
        buttonManageAccount.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ManageAccountActivity.class);
            startActivity(intent);
        });
    }
}