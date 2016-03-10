package com.alxgrk.callconfirm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * 
 * @author alxgrk
 *
 */
public class PhoneCallReceiver extends BroadcastReceiver {

    private static final String TAG = PhoneCallReceiver.class.getSimpleName();

    public static final String PHONE_NUMBER_CODE = "phoneNumber";

    public static final String CALLED_FROM_RECEIVER = "calledFromReceiver";

    private static boolean proceed = false;

    private String phoneNumber;

    public static void skipNextTime() {
        proceed = true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "outgoing call detected");
        phoneNumber = getResultData();
        if (phoneNumber == null) {
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }
        Log.d(TAG, "Trying to call " + phoneNumber);

        if (!proceed) {
            pauseOffhook(context);
        } else {
            proceed = false;
        }
    }

    private void pauseOffhook(Context context) {
        setResultData(null);

        Intent i = new Intent(context, ConfirmationActivity.class);
        Bundle b = new Bundle();
        b.putString(PHONE_NUMBER_CODE, phoneNumber);
        b.putBoolean(CALLED_FROM_RECEIVER, true);
        i.putExtras(b);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }
}