package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.requests.LoginRequest;
import com.example.myapplication.api.responses.LoginResponse;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View 연결
        EditText editTextID = findViewById(R.id.editTextID); // ID 입력 필드
        EditText editTextPassword = findViewById(R.id.editTextPassword); // 비밀번호 입력 필드
        Button buttonLogin = findViewById(R.id.buttonLogin); // 로그인 버튼
        Button buttonSignUp = findViewById(R.id.buttonSignUp); // 회원가입 버튼
        Button buttonFindID = findViewById(R.id.buttonFindID); // 아이디 찾기 버튼
        Button buttonFindPassword = findViewById(R.id.buttonFindPassword); // 비밀번호 찾기 버튼

        // 로그인 버튼 클릭 이벤트
        buttonLogin.setOnClickListener(v -> {
            String userId = editTextID.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (validateInputs(userId, password)) {
                performLogin(userId, password);
            }
        });

        // 회원가입 버튼 클릭 이벤트
        buttonSignUp.setOnClickListener(v -> {
            Log.d(TAG, "Navigating to SignUpActivity...");
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
        // 아이디 찾기 버튼 클릭 이벤트
        buttonFindID.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FindIDActivity.class);
            startActivity(intent);
        });

// 비밀번호 찾기 버튼 클릭 이벤트
        buttonFindPassword.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FindPasswordActivity.class);
            startActivity(intent);
        });

    }

    // 입력 데이터 검증
    private boolean validateInputs(String userId, String password) {
        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // 로그인 요청 처리
    private void performLogin(String userId, String password) {
        Log.d(TAG, "로그인 요청: ID=" + userId + ", Password=" + password);

        LoginRequest request = new LoginRequest(userId, password);

        RetrofitClient.getApiService().login(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(MainActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                        navigateToHomeScreen();
                    } else {
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "네트워크 오류", t);
            }
        });
    }

    // 에러 응답 처리
    private void handleErrorResponse(Response<LoginResponse> response) {
        String errorMessage = "알 수 없는 오류";
        try (ResponseBody errorBody = response.errorBody()) { // UTF-8 디코딩 처리
            if (errorBody != null) {
                String errorBodyString = new String(errorBody.bytes(), StandardCharsets.UTF_8);
                JSONObject errorJson = new JSONObject(errorBodyString);
                errorMessage = errorJson.optString("message", "알 수 없는 오류");
            }
        } catch (Exception e) {
            Log.e(TAG, "에러 메시지 처리 실패", e);
            errorMessage = "서버 응답 처리 중 오류";
        }
        Log.e(TAG, "로그인 실패: " + errorMessage);
        Toast.makeText(MainActivity.this, "로그인 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    // 메인 화면으로 이동
    private void navigateToHomeScreen() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
