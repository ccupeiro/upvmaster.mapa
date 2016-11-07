package com.upvmaster.carlos.mapas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Carlos on 05/11/2016.
 */

public class ReceptorSMS extends BroadcastReceiver {

    public static final String MESSAGE_KEY="mensaje_sms";

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage;
                    if (Build.VERSION.SDK_INT >= 19) { //KITKAT
                        SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                        currentMessage = msgs[i];
                    } else {
                        Object pdus[] = (Object[]) bundle.get("pdus");
                        currentMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    if(phoneNumber.equals("555")){
                        //Arrancar el servicio Musica
                        Intent intent_musica = new Intent(context,ServicioMusica.class);
                        intent_musica.putExtra(MESSAGE_KEY,currentMessage.getDisplayMessageBody().toString());
                        context.startService(intent_musica);
                    }
                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
