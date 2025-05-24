package com.example.aondefica;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ViaCepService {
    @GET("ws/{uf}/{cidade}/{logradouro}/json/")
    Call<List<Endereco>> buscarEndereco(
            @Path("uf") String uf,
            @Path("cidade") String cidade,
            @Path("logradouro") String logradouro
    );
}
