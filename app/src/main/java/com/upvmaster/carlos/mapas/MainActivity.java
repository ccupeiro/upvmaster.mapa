package com.upvmaster.carlos.mapas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final int SOLICITUD_PERMISO_GPS = 0;
    private static final int SOLICITUD_PERMISO_RECEIVE_SMS = 1;

    private View vista;
    private Button btn_mapas;
    private Button btn_sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        vista = findViewById(R.id.content_main);
        btn_mapas = (Button) findViewById(R.id.btn_mapas);
        btn_mapas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    lanzarMapas(view);
                }else{
                    solicitarPermisoGPS();
                }
            }
        });
        btn_sms = (Button) findViewById(R.id.btn_sms);
        btn_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                    lanzarSms(view);
                }else{
                    solicitarPermisoSMS();
                }
            }
        });

        Button arrancar = (Button) findViewById(R.id.boton_arrancar);
        arrancar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startService(new Intent(MainActivity.this,
                        ServicioMusica.class));
            }
        });
        Button detener = (Button) findViewById(R.id.boton_detener);
        detener.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stopService(new Intent(MainActivity.this,
                        ServicioMusica.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void lanzarMapas(View view) {
        //Intent i = new Intent(this, Mapas_Activity.class);
        //startActivity(i);
    }

    private void lanzarSms(View view) {
        Intent i = new Intent(this, MusicOFF_Activity.class);
        startActivity(i);
    }

    void solicitarPermisoGPS() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(vista, "Sin el permiso de acceso al GPS la aplicación no puede funcionar.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, SOLICITUD_PERMISO_GPS);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, SOLICITUD_PERMISO_GPS);
        }
    }

    void solicitarPermisoSMS() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
            Snackbar.make(vista, "Sin el permiso de recepción de SMS no puede funcionar", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, SOLICITUD_PERMISO_RECEIVE_SMS);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, SOLICITUD_PERMISO_RECEIVE_SMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SOLICITUD_PERMISO_GPS:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Llamar a la fnción de GSP
                } else {
                    Snackbar.make(vista, "Sin el permiso, no puedo realizar la acción", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case SOLICITUD_PERMISO_RECEIVE_SMS:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Llamar a la fnción de SMS
                } else {
                    Snackbar.make(vista, "Sin el permiso, no puedo realizar la acción", Snackbar.LENGTH_SHORT).show();
                }
                break;
            default:
                finish();
                break;
        }
    }
}
