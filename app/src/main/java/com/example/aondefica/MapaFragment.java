package com.example.aondefica;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapaFragment extends Fragment implements OnMapReadyCallback {
    private static final String ARG_LAT = "lat";
    private static final String ARG_LNG = "lng";
    private static final String ARG_ENDERECO = "endereco";

    private double latitude;
    private double longitude;
    private String enderecoTexto;
    private MapView mapView;

    public static MapaFragment newInstance(double lat, double lng, String endereco) {
        MapaFragment fragment = new MapaFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LAT, lat);
        args.putDouble(ARG_LNG, lng);
        args.putString(ARG_ENDERECO, endereco);
        fragment.setArguments(args);
        return fragment;
    }

    public MapaFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LAT);
            longitude = getArguments().getDouble(ARG_LNG);
            enderecoTexto = getArguments().getString(ARG_ENDERECO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        if (getArguments() != null) {
            LocationResponse coordenadas = (LocationResponse) getArguments().getSerializable("coordenadas");
            if (coordenadas != null) {
                latitude = Double.parseDouble(coordenadas.lat);
                longitude = Double.parseDouble(coordenadas.lon);
                enderecoTexto = coordenadas.display_name;
            }
        }
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        MapsInitializer.initialize(requireContext());
        LatLng localizacao = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(localizacao).title(enderecoTexto));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacao, 15));
    }

    @Override public void onResume() { super.onResume(); mapView.onResume(); }
    @Override public void onPause() { super.onPause(); mapView.onPause(); }
    @Override public void onDestroy() { super.onDestroy(); mapView.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
}