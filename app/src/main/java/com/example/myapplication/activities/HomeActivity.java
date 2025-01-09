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
        setContentView(R.layout.activity_main);

        Button buttonRecommend = findViewById(R.id.buttonRecommendTrip);
        Button buttonManageAccount = findViewById(R.id.buttonManageAccount);

        buttonRecommend.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, RecommendActivity.class);
            startActivity(intent);
        });

        // 계정 관리 버튼 클릭 시
        buttonManageAccount.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ManageAccountActivity.class);
            startActivity(intent);
        });
    }
}