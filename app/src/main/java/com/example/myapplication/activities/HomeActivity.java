package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // View 연결
        TextView textViewWelcome = findViewById(R.id.textViewWelcome);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        // 환영 메시지 표시
        textViewWelcome.setText("로그인 성공! 메인 화면입니다.");

        // 로그아웃 버튼 클릭 시
        buttonLogout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
