package com.example.myapplication.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.requests.ChangePasswordRequest;
import com.example.myapplication.api.requests.VerificationRequest;
import com.example.myapplication.api.responses.ChangePasswordResponse;
import com.example.myapplication.api.responses.VerificationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // View 연결
        EditText editTextID = findViewById(R.id.editTextID);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextVerificationCode = findViewById(R.id.editTextVerificationCode);
        EditText editTextOldPassword = findViewById(R.id.editTextOldPassword);
        EditText editTextNewPassword = findViewById(R.id.editTextNewPassword);
        EditText editTextConfirmNewPassword = findViewById(R.id.editTextConfirmNewPassword);

        // 버튼을 지역 변수로 선언
        Button buttonSendVerificationCode = findViewById(R.id.buttonSendVerificationCode);
        Button buttonVerifyCode = findViewById(R.id.buttonVerifyCode);
        Button buttonChangePassword = findViewById(R.id.buttonChangePassword);

        // 이메일 인증번호 발송 버튼 클릭 이벤트
        buttonSendVerificationCode.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                showToast("이메일을 입력해주세요.");
                return;
            }

            sendVerificationCode(email);
        });

        // 인증번호 검증 버튼 클릭 이벤트
        buttonVerifyCode.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String code = editTextVerificationCode.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(code)) {
                showToast("이메일과 인증번호를 입력해주세요.");
                return;
            }

            verifyCode(email, code);
        });

        // 비밀번호 변경 버튼 클릭 이벤트
        buttonChangePassword.setOnClickListener(v -> {
            String userId = editTextID.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String verificationCode = editTextVerificationCode.getText().toString().trim();
            String oldPassword = editTextOldPassword.getText().toString().trim();
            String newPassword = editTextNewPassword.getText().toString().trim();
            String confirmNewPassword = editTextConfirmNewPassword.getText().toString().trim();

            if (validateInputs(userId, email, verificationCode, oldPassword, newPassword, confirmNewPassword)) {
                changePassword(userId, email, verificationCode, oldPassword, newPassword);
            }
        });
    }

    private boolean validateInputs(String userId, String email, String verificationCode, String oldPassword, String newPassword, String confirmNewPassword) {
        if (TextUtils.isEmpty(userId)) {
            showToast("아이디를 입력해주세요.");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            showToast("이메일을 입력해주세요.");
            return false;
        }
        if (TextUtils.isEmpty(verificationCode)) {
            showToast("인증번호를 입력해주세요.");
            return false;
        }
        if (TextUtils.isEmpty(oldPassword)) {
            showToast("현재 비밀번호를 입력해주세요.");
            return false;
        }
        if (TextUtils.isEmpty(newPassword)) {
            showToast("새 비밀번호를 입력해주세요.");
            return false;
        }
        if (TextUtils.isEmpty(confirmNewPassword)) {
            showToast("새 비밀번호 확인을 입력해주세요.");
            return false;
        }
        if (newPassword.equals(oldPassword)) {
            showToast("새 비밀번호가 기존 비밀번호와 같습니다.");
            return false;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            showToast("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return false;
        }
        return true;
    }

    private void sendVerificationCode(String email) {
        VerificationRequest request = new VerificationRequest(email);

        RetrofitClient.getApiService().sendVerificationCode(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<VerificationResponse> call, @NonNull Response<VerificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showToast("인증번호가 전송되었습니다: " + response.body().getCode());
                } else {
                    showToast("인증번호 전송 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<VerificationResponse> call, @NonNull Throwable t) {
                showToast("네트워크 오류: " + t.getMessage());
            }
        });
    }

    private void verifyCode(String email, String code) {
        VerificationRequest request = new VerificationRequest(email, code);

        RetrofitClient.getApiService().verifyCode(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<VerificationResponse> call, @NonNull Response<VerificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showToast("인증 성공!");
                } else {
                    showToast("인증 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<VerificationResponse> call, @NonNull Throwable t) {
                showToast("네트워크 오류: " + t.getMessage());
            }
        });
    }

    private void changePassword(String userId, String email, String verificationCode, String oldPassword, String newPassword) {
        ChangePasswordRequest request = new ChangePasswordRequest(userId, email, verificationCode, oldPassword, newPassword);

        RetrofitClient.getApiService().changePassword(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ChangePasswordResponse> call, @NonNull Response<ChangePasswordResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showToast("비밀번호가 성공적으로 변경되었습니다.");
                    finish();
                } else {
                    showToast("비밀번호 변경 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChangePasswordResponse> call, @NonNull Throwable t) {
                showToast("네트워크 오류: " + t.getMessage());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
