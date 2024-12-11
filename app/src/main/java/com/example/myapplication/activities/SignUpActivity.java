package com.example.myapplication.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.requests.CheckIDRequest;
import com.example.myapplication.api.requests.SignUpRequest;
import com.example.myapplication.api.requests.VerificationRequest;
import com.example.myapplication.api.responses.CheckIDResponse;
import com.example.myapplication.api.responses.SignUpResponse;
import com.example.myapplication.api.responses.VerificationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // View 연결
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextVerificationCode = findViewById(R.id.editTextVerificationCode);
        EditText editTextID = findViewById(R.id.editTextID);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        Button buttonSendVerification = findViewById(R.id.buttonSendVerification);
        Button buttonSubmitSignUp = findViewById(R.id.buttonSubmitSignUp);
        Button buttonCheckID = findViewById(R.id.buttonCheckID); // ID 중복 확인 버튼

        // 숫자만 입력 가능하도록 설정
        editTextVerificationCode.setInputType(InputType.TYPE_CLASS_NUMBER);

        // 인증번호 길이 제한 설정 (예: 6자리)
        editTextVerificationCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});

        // 인증번호 요청 버튼 동작
        buttonSendVerification.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                showToast("이메일을 입력해주세요.");
                return;
            }
            sendVerificationCode(email);
        });

        // ID 중복 확인 버튼 동작
        buttonCheckID.setOnClickListener(v -> {
            String id = editTextID.getText().toString().trim();
            if (TextUtils.isEmpty(id)) {
                showToast("아이디를 입력해주세요.");
                return;
            }
            checkID(id);
        });

        // 회원가입 버튼 동작
        buttonSubmitSignUp.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String verificationCode = editTextVerificationCode.getText().toString().trim();
            String id = editTextID.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String passwordConfirm = editTextPasswordConfirm.getText().toString().trim();

            if (validateInputs(email, verificationCode, id, password, passwordConfirm)) {
                sendSignUpRequest(email, verificationCode, id, password);
            }
        });
    }

    // 인증번호 요청
    private void sendVerificationCode(String email) {
        VerificationRequest request = new VerificationRequest(email);

        RetrofitClient.getApiService().sendVerificationCode(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<VerificationResponse> call, @NonNull Response<VerificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showToast("인증번호가 전송되었습니다: " + response.body().getCode());
                } else {
                    showToast("인증번호 요청 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<VerificationResponse> call, @NonNull Throwable t) {
                showToast("네트워크 오류: " + t.getMessage());
                Log.e(TAG, "네트워크 오류", t);
            }
        });
    }

    // ID 중복 확인 요청
    private void checkID(String id) {
        CheckIDRequest request = new CheckIDRequest(id);

        RetrofitClient.getApiService().checkID(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CheckIDResponse> call, @NonNull Response<CheckIDResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showToast(response.body().getMessage());
                    Log.d(TAG, "ID 중복 확인 성공: " + response.body().getMessage());
                } else {
                    showToast("ID 중복 확인 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckIDResponse> call, @NonNull Throwable t) {
                showToast("네트워크 오류: " + t.getMessage());
                Log.e(TAG, "네트워크 오류", t);
            }
        });
    }

    // 회원가입 요청
    private void sendSignUpRequest(String email, String verificationCode, String id, String password) {
        SignUpRequest request = new SignUpRequest(email, verificationCode, id, password);

        RetrofitClient.getApiService().signUp(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<SignUpResponse> call, @NonNull Response<SignUpResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showToast("회원가입 성공: " + response.body().getMessage());
                } else {
                    showToast("회원가입 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignUpResponse> call, @NonNull Throwable t) {
                showToast("네트워크 오류: " + t.getMessage());
            }
        });
    }

    // 입력 검증
    private boolean validateInputs(String email, String verificationCode, String id, String password, String passwordConfirm) {
        if (TextUtils.isEmpty(email)) {
            showToast("이메일을 입력해주세요.");
            return false;
        }
        if (TextUtils.isEmpty(verificationCode)) {
            showToast("인증번호를 입력해주세요.");
            return false;
        }
        if (TextUtils.isEmpty(id)) {
            showToast("아이디를 입력해주세요.");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            showToast("비밀번호를 입력해주세요.");
            return false;
        }
        if (TextUtils.isEmpty(passwordConfirm)) {
            showToast("비밀번호 확인을 입력해주세요.");
            return false;
        }
        if (!password.equals(passwordConfirm)) {
            showToast("비밀번호가 일치하지 않습니다.");
            return false;
        }
        return true;
    }

    // Toast 메시지 표시
    private void showToast(String message) {
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
