package com.example.myapplication.activities;

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
import com.example.myapplication.api.requests.FindPasswordRequest;
import com.example.myapplication.api.responses.FindPasswordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPasswordActivity extends AppCompatActivity {

    private static final String TAG = "FindPasswordActivity";

    // EditText 변수 정의
    private EditText editTextEmail;
    private EditText editTextID;
    private EditText editTextVerificationCode;
    private EditText editTextNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        // EditText와 Button 매핑
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextID = findViewById(R.id.editTextID);
        editTextVerificationCode = findViewById(R.id.editTextVerificationCode);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        Button buttonSendVerificationCode = findViewById(R.id.buttonSendVerificationCode);
        Button buttonResetPassword = findViewById(R.id.buttonResetPassword);

        // 인증번호 발송 버튼 클릭 리스너
        buttonSendVerificationCode.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            sendVerificationCode(email);
        });

        // 비밀번호 재설정 버튼 클릭 리스너
        buttonResetPassword.setOnClickListener(v -> {
            String userId = editTextID.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String verificationCode = editTextVerificationCode.getText().toString().trim();
            String newPassword = editTextNewPassword.getText().toString().trim();

            if (validateInputs(userId, email, verificationCode, newPassword)) {
                resetPassword(userId, email, verificationCode, newPassword);
            }
        });
    }

    private void sendVerificationCode(String email) {
        // 인증번호 요청 객체 생성
        FindPasswordRequest request = new FindPasswordRequest(null, email, null, null);

        RetrofitClient.getApiService().sendPasswordVerificationCode(request)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(FindPasswordActivity.this, "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FindPasswordActivity.this, "인증번호 발송 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(FindPasswordActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error: ", t);
                    }
                });
    }

    private void resetPassword(String userId, String email, String verificationCode, String newPassword) {
        // 비밀번호 재설정 요청 객체 생성 (4개의 매개변수)
        FindPasswordRequest request = new FindPasswordRequest(userId, email, verificationCode, newPassword);

        // Retrofit 요청 전송
        RetrofitClient.getApiService().resetPassword(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<FindPasswordResponse> call, @NonNull Response<FindPasswordResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(FindPasswordActivity.this, "비밀번호가 재설정되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FindPasswordActivity.this, "비밀번호 재설정 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FindPasswordResponse> call, @NonNull Throwable t) {
                Toast.makeText(FindPasswordActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error: ", t);
            }
        });
    }

    private boolean validateInputs(String userId, String email, String verificationCode, String newPassword) {
        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(verificationCode)) {
            Toast.makeText(this, "인증번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, "새 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
