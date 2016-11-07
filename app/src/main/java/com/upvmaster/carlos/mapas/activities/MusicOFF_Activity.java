package com.upvmaster.carlos.mapas.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.upvmaster.carlos.mapas.R;
import com.upvmaster.carlos.mapas.services.ServicioMusica;

public class MusicOFF_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_off);
        Button detener = (Button) findViewById(R.id.music_off);
        detener.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stopService(new Intent(MusicOFF_Activity.this,
                        ServicioMusica.class));
            }
        });
        Button mapa = (Button) findViewById(R.id.mapa_music);
        mapa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(MusicOFF_Activity.this, Mapa_Activity.class);
                startActivity(i);
            }
        });
    }
}
