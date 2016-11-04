package com.upvmaster.carlos.mapas;

import android.Manifest;
import android.app.Activity;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.upvmaster.carlos.mapas.R.id.mapa;

public class Mapa_Activity extends FragmentActivity implements OnMapReadyCallback {

    private static final int SOLICITUD_PERMISO_GPS = 0;


    private Activity activity;
    private View vista;
    private Location localizacion = null;
    private Marker marker;
    private String provider;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private MiLocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(mapa);
        vista = findViewById(R.id.vista_mapa);
        activity = this;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Criteria cri = new Criteria();
                cri.setAccuracy(Criteria.ACCURACY_FINE);
                provider = locationManager.getBestProvider(cri, false);
                if (provider != null & !provider.equals("")) {
                    locationListener = new MiLocationListener();
                    //TODO hay que hacer los dos listener para ver que lanza
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LocalizarTask().execute();
    }

    @Override
    protected void onPause() {
        if(locationManager!=null){
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(locationListener);
            }else{
                locationManager = null;
            }

        }
        super.onPause();
    }

    void solicitarPermisoGPS() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make (activity.getCurrentFocus(), "Sin el permiso de acceso al GPS la aplicación no puede funcionar.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(Mapa_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, SOLICITUD_PERMISO_GPS);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, SOLICITUD_PERMISO_GPS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SOLICITUD_PERMISO_GPS:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //iniciarGPS();
                } else {
                    Snackbar.make(vista, "Sin el permiso, no puedo realizar la acción", Snackbar.LENGTH_SHORT).show();
                }
                break;
            default:
                finish();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setCompassEnabled(true);
            moverCamara();
        }else{
            solicitarPermisoGPS();
        }
    }
    private void moverCamara(){
        if(mMap!=null){
            LatLng posicion = new LatLng(localizacion.getLatitude(),localizacion.getLongitude());
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicion,15));
            if(marker!=null){
                marker.remove();
            }
            marker = mMap.addMarker(new MarkerOptions().position(posicion));
        }
    }

    //Dialogo Reinicio
    private void mostrarDialogoReinicio(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.dialog_reset, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button llamar al asynctasks
                new LocalizarTask().execute();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private class MiLocationListener implements android.location.LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            localizacion = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    }


    private class LocalizarTask extends AsyncTask<Void, Void, Boolean>{

        ProgressDialog pd;
        Context context;

        @Override
        protected void onPreExecute() {
            context = getApplicationContext();
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                pd = new ProgressDialog(activity);
                pd.setCancelable(false);
                pd.setMessage("Cargando la ubicación...");
                pd.show();
            }else{
                cancel(true);
            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (isCancelled()) return false;
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        long startTime = System.currentTimeMillis();
                        while (localizacion == null) {
                            long currentTime = System.currentTimeMillis();
                            if ((currentTime - startTime) > 30000)
                                break;
                        }
                        return localizacion!=null;
            }
            return false;
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
            solicitarPermisoGPS();
        }

        @Override
        protected void onPostExecute(Boolean resul) {
            if(resul){
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //Bucle para buscar
                    if(pd!=null) pd.dismiss();
                    if(localizacion!=null) {
                        mapFragment.getMapAsync((OnMapReadyCallback) activity);
                    }else{
                        mostrarDialogoReinicio();
                    }

                }else{
                    if(pd!=null) pd.dismiss();
                    mostrarDialogoReinicio();
                }

            }else{
               if(pd!=null) pd.dismiss();
                mostrarDialogoReinicio();
            }
        }
    }
}
