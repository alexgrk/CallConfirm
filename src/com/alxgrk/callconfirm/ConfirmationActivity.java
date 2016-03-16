/*
 * Created on 20.02.2016
 *
 * author Alex
 */
package com.alxgrk.callconfirm;

import static com.alxgrk.callconfirm.PhoneCallReceiver.CALLED_FROM_RECEIVER;
import static com.alxgrk.callconfirm.PhoneCallReceiver.PHONE_NUMBER_CODE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * @author alxgrk
 *
 */
public class ConfirmationActivity extends Activity implements OnCancelListener {

    public static final String TAG = ConfirmationActivity.class.getSimpleName();

    private String phoneNumber;

    private LanguageControl lang;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lang = LanguageControl.getInstance(getApplicationContext());

        boolean wasStartedByPhoneReceiver = calledFromReceiver();
        Log.d(TAG, "(onCreate) called from receiver: " + wasStartedByPhoneReceiver);

        if (wasStartedByPhoneReceiver) {
            phoneNumber = getIntent().getStringExtra(PHONE_NUMBER_CODE);

            if (phoneNumber != null) {
                showDialog();
            } else {
                Log.e(TAG, lang.error());
                Toast.makeText(this, lang.error(), Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Log.d(TAG, "Not called by PhoneCallReceiver, redirecting to MainActivity.");
            startMainActivity();
        }
    }

    private boolean calledFromReceiver() {
        return getIntent().getBooleanExtra(CALLED_FROM_RECEIVER, false);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "(onResume) called from receiver: " + calledFromReceiver());
        if (!calledFromReceiver()) {
            startMainActivity();
        }
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "(onRestart) called from receiver: " + calledFromReceiver());
        if (!calledFromReceiver()) {
            startMainActivity();
        }
        super.onRestart();
    }

    private void startMainActivity() {
        Log.d(TAG, "starting MainActivity");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(CALLED_FROM_RECEIVER, false);
        startActivity(intent);
        finish();
    }

    private void showDialog() {
        Log.d(TAG, phoneNumber);

        new AlertDialog.Builder(this).setTitle(lang.confirmation()).setPositiveButton(lang.yes(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int flag) {
                        // preserve number for dialog
                        String localPhoneNumber = phoneNumber;
                        PhoneCallReceiver.skipNextTime();

                        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                                + localPhoneNumber));
                        startActivity(i);
                        finish();
                    }
                }).setNegativeButton(lang.no(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int flag) {
                        dialogInterface.cancel();
                    }
                }).setOnCancelListener(this).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "(onSaveInstanceState) called from receiver: " + calledFromReceiver());
        outState.putBoolean(CALLED_FROM_RECEIVER, false);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        finish();
    }
}