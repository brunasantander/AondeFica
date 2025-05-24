package com.example.aondefica;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocationService {
    @GET("search.php")
    Call<List<LocationResponse>> buscarCoordenadas(
            @Query("key") String apiKey,
            @Query("q") String enderecoCompleto,
            @Query("format") String format
    );
}