package com.example.aondefica;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuscarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuscarFragment extends Fragment {
    private Spinner spinnerEstado, spinnerCidade;
    private EditText editTextReferencia;
    private Button buttonBuscar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ResultadosFragment resultadosFragment = new ResultadosFragment();

    private final String[] estados = {"Selecione", "SP", "RJ", "MG"};
    private final Map<String, String[]> cidadesPorEstado = new HashMap<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BuscarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuscarFragment newInstance(String param1, String param2) {
        BuscarFragment fragment = new BuscarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentManager=getParentFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        return inflater.inflate(R.layout.fragment_buscar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        spinnerEstado = view.findViewById(R.id.spinner_estado);
        spinnerCidade = view.findViewById(R.id.spinner_cidade);
        editTextReferencia = view.findViewById(R.id.edittext_referencia);
        buttonBuscar = view.findViewById(R.id.button_buscar);
        carregarEstados();
        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ufSelecionado = (String) parent.getItemAtPosition(position);
                if (!ufSelecionado.equals("Selecione")) {
                    carregarMunicipios(ufSelecionado);
                } else {
                    spinnerCidade.setAdapter(null);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // faz nada
            }
        });
        buttonBuscar.setOnClickListener(v -> {buscar();});

    }

    private void carregarEstados() {
        Call<List<Estado>> call = new RetrofitConfig()
                .servicosIBGE()
                .getEstados();

        call.enqueue(new Callback<List<Estado>>() {
            @Override
            public void onResponse(Call<List<Estado>> call, Response<List<Estado>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Estado> estados = response.body();
                    List<String> nomesEstados = new ArrayList<>();
                    nomesEstados.add("Selecione");

                    if (estados != null) {
                        for (Estado estado : estados) {
                            nomesEstados.add(estado.getSigla());
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            nomesEstados
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEstado.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Estado>> call, Throwable t) {
                Context context = getContext();
                Toast.makeText(context, "Falha ao buscar estados", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarMunicipios(String uf) {
        Call<List<Municipio>> call = new RetrofitConfig()
                .servicosIBGE()
                .getMunicipios(uf);

        call.enqueue(new Callback<List<Municipio>>() {
            @Override
            public void onResponse(Call<List<Municipio>> call, Response<List<Municipio>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Municipio> municipios = response.body();
                    List<String> nomesCidades = new ArrayList<>();
                    nomesCidades.add("Selecione");

                    for (Municipio municipio : municipios) {
                        nomesCidades.add(municipio.getNome());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            nomesCidades
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCidade.setAdapter(adapter);
                } else {
                    Toast.makeText(requireContext(), "Erro ao carregar cidades", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Municipio>> call, Throwable t) {
                Context context = getContext();
                Toast.makeText(context, "Falha ao buscar as cidades", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscar() {
        String uf = (String) spinnerEstado.getSelectedItem();
        String cidade = (String) spinnerCidade.getSelectedItem();
        String referencia = editTextReferencia.getText().toString().trim();

        if (uf.equals("Selecione") || cidade == null || cidade.isEmpty() || referencia.isEmpty()) {
            Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<List<Endereco>> call = new RetroFitViaCEPConfig()
                .servicoViaCep().buscarEndereco(uf, cidade, referencia);

        call.enqueue(new Callback<List<Endereco>>() {
            @Override
            public void onResponse(Call<List<Endereco>> call, Response<List<Endereco>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Endereco> enderecos = response.body();
                    if (enderecos.isEmpty()) {
                        Toast.makeText(requireContext(), "Nenhum endereço encontrado", Toast.LENGTH_SHORT).show();
                    } else {
                        StringBuilder resultado = new StringBuilder();
                        for (Endereco e : enderecos) {
                            resultado.append(e.getLogradouro()).append(", ")
                                    .append(e.getBairro()).append(", ")
                                    .append(e.getLocalidade()).append(" - ")
                                    .append(e.getUf()).append(" (CEP: ").append(e.getCep()).append(")\n\n");
                        }
                        if (!enderecos.isEmpty()) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("enderecos", (Serializable) enderecos);

                            resultadosFragment.setArguments(bundle);
                            trocarFragment(resultadosFragment);
                        } else {
                            Toast.makeText(requireContext(), "Nenhum endereço encontrado", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Erro na resposta da API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Endereco>> call, Throwable t) {
                Context context = getContext();
                Toast.makeText(requireContext(), "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void trocarFragment(Fragment fragment) {
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}