package com.example.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
        buttonResetPassword.setOnClickListener(v -> startActivity(new Intent(ManageAccountActivity.this, ResetPasswordActivity.class)));

        // 로그아웃 버튼 클릭 이벤트
        if (buttonLogout != null) { // 버튼이 null이 아닌 경우에만 설정
            buttonLogout.setOnClickListener(v -> performLogout());
        }
    }

    private void performLogout() {
        // SharedPreferences 초기화 (세션 삭제)
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // 로그아웃 메시지
        Toast.makeText(ManageAccountActivity.this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

        // 메인 화면으로 이동
        Intent intent = new Intent(ManageAccountActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // 현재 액티비티 종료
        finish();
    }
}
