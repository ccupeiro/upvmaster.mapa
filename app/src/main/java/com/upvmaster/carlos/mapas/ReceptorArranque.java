package com.upvmaster.carlos.mapas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
