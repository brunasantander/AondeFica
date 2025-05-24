package com.example.aondefica;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultadosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultadosFragment extends Fragment {

    private static final String ARG_ENDERECOS = "enderecos";
    private ArrayList<Endereco> listaEnderecos;

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
    }
}