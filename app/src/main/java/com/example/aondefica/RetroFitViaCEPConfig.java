package com.example.aondefica;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitViaCEPConfig {
    private Retrofit retrofit;

    public RetroFitViaCEPConfig() {
        this.retrofit = new Retrofit.Builder().baseUrl("https://viacep.com.br/").addConverterFactory(GsonConverterFactory.create()).build();
    }

    public ViaCepService servicoViaCep () {
        return this.retrofit.create(ViaCepService.class);
    }
}
