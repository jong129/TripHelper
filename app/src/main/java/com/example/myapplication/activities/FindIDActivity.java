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
import com.example.myapplication.api.requests.FindIDRequest;
import com.example.myapplication.api.requests.VerificationRequest;
import com.example.myapplication.api.responses.FindIDResponse;
import com.example.myapplication.api.responses.VerificationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindIDActivity extends AppCompatActivity {

    private static final String TAG = "FindIDActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextVerificationCode = findViewById(R.id.editTextVerificationCode);
        Button buttonSendVerification = findViewById(R.id.buttonSendVerificationCode);
        Button buttonRetrieveID = findViewById(R.id.buttonFindID);
        Button buttonVerifyCode = findViewById(R.id.buttonVerifyCode);

        buttonSendVerification.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            sendVerificationCode(email);
        });

        buttonVerifyCode.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String code = editTextVerificationCode.getText().toString().trim();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(code)) {
                Toast.makeText(this, "이메일과 인증번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            verifyCode(email, code);
        });

        buttonRetrieveID.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String verificationCode = editTextVerificationCode.getText().toString().trim();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(verificationCode)) {
                Toast.makeText(this, "이메일과 인증번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            findID(email, verificationCode);
        });
    }

    private void sendVerificationCode(String email) {
        RetrofitClient.getApiService().sendIDVerificationCode(new FindIDRequest(email))
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(FindIDActivity.this, "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FindIDActivity.this, "인증번호 발송 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(FindIDActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error: ", t);
                    }
                });
    }

    private void verifyCode(String email, String code) {
        VerificationRequest request = new VerificationRequest(email, code);
        RetrofitClient.getApiService().verifyCode(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<VerificationResponse> call, @NonNull Response<VerificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(FindIDActivity.this, "인증 성공!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FindIDActivity.this, "인증 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<VerificationResponse> call, @NonNull Throwable t) {
                Toast.makeText(FindIDActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findID(String email, String verificationCode) {
        FindIDRequest request = new FindIDRequest(email, verificationCode);
        RetrofitClient.getApiService().findID(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<FindIDResponse> call, @NonNull Response<FindIDResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(FindIDActivity.this, "아이디: " + response.body().getUserId(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FindIDActivity.this, "아이디 찾기 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FindIDResponse> call, @NonNull Throwable t) {
                Toast.makeText(FindIDActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error: ", t);
            }
        });
    }
}
