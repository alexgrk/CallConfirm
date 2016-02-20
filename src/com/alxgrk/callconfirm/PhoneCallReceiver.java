package com.alxgrk.callconfirm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class PhoneCallReceiver extends BroadcastReceiver {

    public static final String PHONE_NUMBER_CODE = "phoneNumber";

    public static boolean proceed = false;

    private String phoneNumber;

    private void pauseOffhook(Context context) {
        setResultData(null);

        Intent i = new Intent(context, MainActivity.class);
        Bundle b = new Bundle();
        b.putString(PHONE_NUMBER_CODE, phoneNumber);
        i.putExtras(b);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("PhoneCallReceiver", "outgoing call detected");
        phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        Log.d("PhoneCallReceiver", "Trying to call " + phoneNumber);

        if (!proceed) {
            pauseOffhook(context);
            return;
        }
        proceed = false;
    }
}