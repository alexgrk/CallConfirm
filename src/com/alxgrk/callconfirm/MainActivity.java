package com.alxgrk.callconfirm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements DialogInterface.OnCancelListener {
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        phoneNumber = getIntent().getStringExtra(PhoneCallReceiver.PHONE_NUMBER_CODE);

        if (phoneNumber != null) {
            showDialog();
        } else {
            setContentView(R.layout.main_activity);

            ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleActivation);
            toggle.setChecked(true);
            toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d("MainActivity", "toggle state changed");
                }
            });
        }
    }

    private void showDialog() {
        Log.d("MainActivity", phoneNumber);

        new AlertDialog.Builder(this).setTitle("Diesen Anruf wirklich tätigen?").setPositiveButton(
                "Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int flag) {
                        PhoneCallReceiver.proceed = true;
                        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        startActivity(i);
                        finish();
                    }
                }).setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int flag) {
                        dialogInterface.cancel();
                    }
                }).setOnCancelListener(this).show();
    }

    @Override
    public void onCancel(DialogInterface paramDialogInterface) {
        finish();
    }
}