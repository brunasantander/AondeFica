package com.example.aondefica;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IBGEService {
    @GET("localidades/estados")
    Call<List<Estado>> getEstados();

    @GET("localidades/estados/{uf}/municipios")
    Call<List<Municipio>> getMunicipios(@Path("uf") String uf);
}
