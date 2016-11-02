package com.upvmaster.carlos.mapas;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.upvmaster.carlos.mapas.R.id.mapa;

public class Mapas_Activity extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private View vista;
    private GoogleMap mMap;
    private double latitude,longitude;
    private Boolean cargando;
    protected LocationManager locationManager;
    private String provider;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);
        cargando = true;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(mapa);
        mapFragment.getMapAsync(this);
        vista = findViewById(R.id.vista_mapa);
    }

    private void iniciarGPS(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //Toast.makeText(getApplicationContext(),"Hay que buscar señal. Vamos a Esperar a movernos!!!",Toast.LENGTH_LONG ).show();
            //Que el GPS esté activo
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                Criteria cri=new Criteria();
                provider=locationManager.getBestProvider(cri,false);
                if(provider!=null & !provider.equals("")){
                    Location location=locationManager.getLastKnownLocation(provider);
                    locationManager.requestLocationUpdates(provider, 4000, 20, this);
                    if(location!=null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        cargando=false;
                    }else{
                        cargando = true;
                        Toast.makeText(getApplicationContext(),"Buscando señal",Toast.LENGTH_LONG ).show();
                    }
                }else{
                    mostrarDialogoReinicio();
                }

            }else{
                mostrarDialogoReinicio();
            }

        }
    }

    private void moverCamara(LatLng posicion){
        if(mMap!=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicion,15));
            if(marker!=null){
                marker.remove();
            }
            marker = mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target));
        }
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            iniciarGPS();
        }

    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(locationManager!=null)
                locationManager.removeUpdates(this);
        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setCompassEnabled(true);
            if(!cargando){
                moverCamara(new LatLng(latitude,longitude));
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        if(mMap!=null){
            if(cargando){
                Toast.makeText(getApplicationContext(),"Nos Movemos",Toast.LENGTH_SHORT ).show();
                cargando = false;
            }
            LatLng posicion = new LatLng(location.getLatitude(),location.getLongitude());
            moverCamara(posicion);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //Dialogo Reinicio
    private void mostrarDialogoReinicio(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.dialog_reset, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                iniciarGPS();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                finish();
            }
        });
        builder.show();
    }
}
