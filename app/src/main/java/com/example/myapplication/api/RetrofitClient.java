package com.example.myapplication.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // Flask 서버의 IP 주소와 포트를 설정
    private static final String BASE_URL = "http://192.168.0.2:5000"; // Flask 서버 IP

    private static Retrofit retrofit = null;

    // Retrofit 클라이언트 생성 메서드
    public static Retrofit getClient() {
        if (retrofit == null) {
            // HTTP 로그 인터셉터 추가
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 요청/응답 바디 로깅

            // OkHttpClient 생성
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor) // 로깅 인터셉터 추가
                    .build();

            // Retrofit 인스턴스 생성
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // 서버 URL
                    .client(client) // OkHttpClient 사용
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 변환기
                    .build();
        }
        return retrofit;
    }

    // ApiService 인터페이스 반환
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}
