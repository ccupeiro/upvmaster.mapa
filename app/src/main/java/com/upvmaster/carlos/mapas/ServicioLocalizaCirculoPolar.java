package com.upvmaster.carlos.mapas;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Carlos on 07/11/2016.
 */

public class ServicioLocalizaCirculoPolar extends Service {

    private final static double LATITUD_CIRCULO_POLAR = 66.55f;
    private static final int ID_NOTIFICACION_CREAR = 2;
    private Context context;
    private LocationManager locationManager;
    private MiLocationListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                listener = new MiLocationListener();
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                }
            }else{
                Toast.makeText(getApplicationContext(),"No se puede cargar la ubicación",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Se necesitan permisos para la aplicación Mapas",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(listener);
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class MiLocationListener implements android.location.LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            if(estaEnCirculoPolar(location)){
                PendingIntent pIntentMapa = PendingIntent.getActivity( context, 0,new Intent(context, Mapa_Activity.class) , 0);
                //Notificacion
                NotificationCompat.Builder notific = new NotificationCompat.Builder(context)
                        .setContentTitle("Creando Servicio de Circulo Polar ártico")
                        .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                        .setContentText("Ppulse para ir a ver el Mapa")
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setDefaults(Notification.DEFAULT_LIGHTS);
                notific.setContentIntent(pIntentMapa);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(ID_NOTIFICACION_CREAR, notific.build());
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    }

    private boolean estaEnCirculoPolar(Location location){
        return location.getLatitude()>=LATITUD_CIRCULO_POLAR;
    }
}
