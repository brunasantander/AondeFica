package com.example.aondefica;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    private Retrofit retrofit;

    public RetrofitConfig() {
        this.retrofit = new Retrofit.Builder().baseUrl("https://servicodados.ibge.gov.br/api/v1/").addConverterFactory(GsonConverterFactory.create()).build();
    }

    public IBGEService servicosIBGE() {
        return this.retrofit.create(IBGEService.class);
    }

}