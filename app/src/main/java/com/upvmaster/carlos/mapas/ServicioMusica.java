package com.upvmaster.carlos.mapas;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by Carlos on 05/11/2016.
 */

public class ServicioMusica extends Service {

    private static final int ID_NOTIFICACION_CREAR = 1;

    MediaPlayer reproductor;

    @Override
    public void onCreate() {
        reproductor = MediaPlayer.create(this, R.raw.monkey);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PendingIntent pIntentMapa = PendingIntent.getActivity( this, 0, new Intent(this, Mapa_Activity.class), 0);
        PendingIntent pIntentMusicaOff = PendingIntent.getActivity( this, 0, new Intent(this, MusicOFF_Activity.class), 0);

        NotificationCompat.Builder notific = new NotificationCompat.Builder(this)
                .setContentTitle("Creando Servicio de Música")
                .setSmallIcon(R.drawable.ic_music_player)
                .setContentText("Pulsa para poder Quitar la música")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_LIGHTS);
        notific.addAction(android.R.drawable.ic_menu_mylocation, "Call", pIntentMapa);
        notific.setContentIntent(pIntentMusicaOff);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_NOTIFICACION_CREAR, notific.build());
        reproductor.start();
        return START_STICKY;
    }

    @Override public void onDestroy() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ID_NOTIFICACION_CREAR);
        reproductor.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
