package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class ManageAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        // 버튼 연결
        Button buttonResetPassword = findViewById(R.id.buttonResetPassword);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        // 비밀번호 재설정 버튼 클릭 이벤트
        buttonResetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(ManageAccountActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });

        // 로그아웃 버튼 클릭 이벤트
        buttonLogout.setOnClickListener(v -> {
            // 로그아웃 처리 (예: SharedPreferences 초기화, 세션 종료 등)
            Toast.makeText(ManageAccountActivity.this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

            // 메인 화면으로 이동
            Intent intent = new Intent(ManageAccountActivity.this, MainActivity.class);

            // 플래그 설정: 이전 모든 액티비티를 종료하고 새 작업 시작
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

            // 현재 액티비티 종료
            finish();
        });
    }
}
