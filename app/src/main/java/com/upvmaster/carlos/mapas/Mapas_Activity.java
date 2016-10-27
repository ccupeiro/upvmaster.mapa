package com.upvmaster.carlos.mapas;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapas_Activity extends FragmentActivity implements OnMapReadyCallback {

    private View vista;
    private GoogleMap mMap;
    private final LatLng MADRID = new LatLng(39.481106, -0.340987);
    private LatLng posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        vista = findViewById(R.id.vista_mapa);
        Button btn_colocar = (Button) findViewById(R.id.btn_colocar);
        btn_colocar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateCamera();
            }
        });

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            posicion = new LatLng(latitude,longitude);
        }else{
            posicion = MADRID;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 15));
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getPos(), 15));
            mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target));
            animateCamera();
        }

    }

    public LatLng getPos() {
        if (mMap.getMyLocation() != null) {
            return new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
        } else {
            return MADRID;
        }

    }

    public void animateCamera() {
        if (mMap.getMyLocation() != null)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()), 15));
    }
}
