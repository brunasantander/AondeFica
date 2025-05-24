package com.example.aondefica;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultadosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultadosFragment extends Fragment {

    private static final String ARG_ENDERECOS = "enderecos";
    private ArrayList<Endereco> listaEnderecos;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MapaFragment mapaFragment = new MapaFragment();

    public static ResultadosFragment newInstance(ArrayList<Endereco> enderecos) {
        ResultadosFragment fragment = new ResultadosFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_ENDERECOS, enderecos);
        fragment.setArguments(args);
        return fragment;
    }

    public ResultadosFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listaEnderecos = getArguments().getParcelableArrayList(ARG_ENDERECOS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            ArrayList<Endereco> enderecos = (ArrayList<Endereco>) getArguments().getSerializable("enderecos");
            if (enderecos != null) {
                listaEnderecos = enderecos;
            }
        }
        fragmentManager=getParentFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        return inflater.inflate(R.layout.fragment_resultados, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ListView listView = view.findViewById(R.id.listview_resultados);
        if (listaEnderecos != null) {
            List<String> listaTexto = new ArrayList<>();
            for (Endereco e : listaEnderecos) {
                listaTexto.add(e.getLogradouro() + ", " + e.getBairro() + ", " +
                        e.getLocalidade() + " - " + e.getUf() + " (CEP: " + e.getCep() + ")");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    listaTexto
            );
            listView.setAdapter(adapter);
        }
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Endereco enderecoSelecionado = listaEnderecos.get(position);
            buscarCoordenadasEExibir(enderecoSelecionado);
        });
    }

    private void buscarCoordenadasEExibir(Endereco endereco) {
        String enderecoCompleto = endereco.getLogradouro() + ", " +
                endereco.getLocalidade() + ", " +
                endereco.getUf();

        LocationService service = RetrofitConfig.getLocationIQRetrofit().create(LocationService.class);
        Call<List<LocationResponse>> call = service.buscarCoordenadas(
                "pk.07f0f9f7672b3b6913de7ffee118262f",
                enderecoCompleto,
                "json"
        );

        call.enqueue(new Callback<List<LocationResponse>>() {
            @Override
            public void onResponse(Call<List<LocationResponse>> call, Response<List<LocationResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    LocationResponse geo = response.body().get(0);
                    geo.display_name = enderecoCompleto;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("coordenadas", (Serializable) geo);

                    mapaFragment.setArguments(bundle);
                    trocarFragment(mapaFragment);
                } else {
                    Toast.makeText(requireContext(), "Localização não encontrada", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LocationResponse>> call, Throwable t) {
                Toast.makeText(requireContext(), "Erro ao buscar coordenadas", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void trocarFragment(Fragment fragment) {
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}