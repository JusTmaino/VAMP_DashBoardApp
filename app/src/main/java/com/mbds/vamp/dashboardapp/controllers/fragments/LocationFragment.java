package com.mbds.vamp.dashboardapp.controllers.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mbds.vamp.dashboardapp.R;
import com.mbds.vamp.dashboardapp.utils.ItineraireTask;

import java.io.IOException;
import java.util.ArrayList;


public class LocationFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap googleMap;
    MapView mapView;
    private static View view;
    private GoogleMap gMap;
    private final ArrayList<LatLng> lstLatLng = new ArrayList<LatLng>();

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
            view = null;
        }
        try {

            view = inflater.inflate(R.layout.fragment_location, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        //  }

        view = inflater.inflate(R.layout.fragment_location, container, false);
        return view;
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_controls, container, false);
        return rootView;
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        this.googleMap = googleMap;
        //GEOLOCALICATION DU VEHICUL
        double latD = 43.6188256;
        double lngD = 7.0570129999999835;

        //ADRESSE DE L'UTILISATEUR
        double latA = 43.606548;
        double lngA = 7.123815199999967;

        //LES POIS (POINT DE CHARGE) A RECUPERER DE L'API
        lstLatLng.add(new LatLng(43.6172352,7.06450760000007));
        lstLatLng.add(new LatLng(43.6171231 , 7.050360500000011));
        lstLatLng.add(new LatLng(43.62277599999999, 7.058908200000019));
        lstLatLng.add(new LatLng(43.6245433, 7.0614014000000225));
        lstLatLng.add(new LatLng(43.6137472 , 7.058677699999976));
        lstLatLng.add(new LatLng(43.6220652, 7.0626263000000336));
        lstLatLng.add(new LatLng(43.62850100000001, 7.042699999999968));

        //RECHERCHE DE L'ITINERAIRE
        new ItineraireTask(getActivity().getApplicationContext(), this.googleMap, new LatLng(latD, lngD), new LatLng(latA, lngA),lstLatLng).execute();

      /*  googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Nice, France ").snippet("Latitude : "+lat +"| Longitude : "+lng));
        CameraPosition liberty = CameraPosition.builder().target(new LatLng(lat, lng)).zoom(20).bearing(0).tilt(45).build();
        float zoomLevel = 10.0f; //This goes up to 21

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoomLevel));
       /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);*/
       // googleMap.getUiSettings().setZoomControlsEnabled(true);
    }


}
