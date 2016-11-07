package com.upvmaster.carlos.mapas.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.upvmaster.carlos.mapas.services.ServicioLocalizaCirculoPolar;

/**
 * Created by Carlos on 07/11/2016.
 */

public class ReceptorArranque extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("POLAR--","Se inicia el servicio");
        context.startService(new Intent(context, ServicioLocalizaCirculoPolar.class));
    }
}
