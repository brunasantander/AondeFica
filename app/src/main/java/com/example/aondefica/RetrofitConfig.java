package com.example.aondefica;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    private Retrofit retrofit;

    public RetrofitConfig() {
        this.retrofit = new Retrofit.Builder().baseUrl("https://servicodados.ibge.gov.br/api/v1/").addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static Retrofit getLocationIQRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://us1.locationiq.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public IBGEService servicosIBGE() {
        return this.retrofit.create(IBGEService.class);
    }

}