package com.danifitrianto.diskussin.setups.retrofit;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static ApiClient instance = null;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static final String BASE_URL = "http://203.194.112.127/api/";

    public static <S> S createService(Class<S> serviceClass,@Nullable final String authToken) {
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit =builder.build();

        if (!TextUtils.isEmpty(authToken)) {
            TokenInterceptor interceptor =
                    new TokenInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }
        return retrofit.create(serviceClass);
    }

}
