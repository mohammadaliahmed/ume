package com.umetechnologypvt.ume.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppConfig {
     static String BASE_URL = "http://acnure.com/";

    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
