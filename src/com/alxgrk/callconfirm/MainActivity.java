package com.alxgrk.callconfirm;

import com.alxgrk.callconfirm.Preferences.ActivationState;
import com.alxgrk.callconfirm.Preferences.Language;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 
 * @author alxgrk
 *
 */
public class MainActivity extends Activity implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ToggleButton toggle;

    private Preferences preferences;

    private ImageView iV_DE;

    private ImageView iV_EN;

    private LanguageControl lang;

    private TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        preferences = new Preferences(getApplicationContext());
        boolean isActivated = ActivationState.ON == preferences.getActivationState() ? true : false;

        lang = LanguageControl.getInstance(getApplicationContext());

        final PermissionChecker permissionChecker = new PermissionChecker(this);
        boolean hasPermissions = ensurePermissionsGranted(permissionChecker);

        label = (TextView) findViewById(R.id.tV_label);

        toggle = (ToggleButton) findViewById(R.id.toggleActivation);
        toggle.setChecked(hasPermissions && isActivated);
        toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "toggle state changed");
                if (isChecked) {
                    ensurePermissionsGranted(permissionChecker);
                    preferences.setActivationState(ActivationState.ON);
                    toggle.setText(lang.on());
                } else {
                    preferences.setActivationState(ActivationState.OFF);
                    toggle.setText(lang.off());
                }
            }
        });

        iV_DE = (ImageView) findViewById(R.id.iV_DE);
        iV_DE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked(Language.GERMAN);
            }
        });

        iV_EN = (ImageView) findViewById(R.id.iV_EN);
        iV_EN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked(Language.ENGLISH);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadLanguagePref();
        label.setText(lang.toggleLabel());
        toggle.setTextOn(lang.on());
        toggle.setTextOff(lang.off());
    }

    private void loadLanguagePref() {
        switch (preferences.getLanguage()) {
            case ENGLISH:
                greyOut(iV_DE);
                break;
            case GERMAN:
                greyOut(iV_EN);
                break;
        }
    }

    private boolean ensurePermissionsGranted(PermissionChecker permissionChecker) {
        boolean hasPermissions = permissionChecker.hasPermissions();
        if (!hasPermissions) {
            Log.d(TAG, "asking user for permissions");
            permissionChecker.askUserForMissingPermissions();
        }
        return hasPermissions;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
            int[] grantResults) {
        if (requestCode == PermissionChecker.PERMISSION_REQUEST_CODE) {
            boolean grantedAll = true;
            int count = 0;
            for (String permission : permissions) {
                boolean grantResult = grantResults[count++] == PackageManager.PERMISSION_GRANTED;
                grantedAll &= grantResult;
                Log.d(TAG, "granted permission " + permission + ": " + grantResult);
            }
            toggle.setChecked(grantedAll);
        }
    }

    private void clicked(Language language) {
        preferences.setLanguage(language);
        switch (language) {
            case ENGLISH:
                restoreOriginal(iV_EN);
                greyOut(iV_DE);
                break;
            case GERMAN:
                restoreOriginal(iV_DE);
                greyOut(iV_EN);
                break;
        }
        label.setText(lang.toggleLabel());
        toggle.setTextOn(lang.on());
        toggle.setTextOff(lang.off());
        updateToggle();
    }

    private void greyOut(ImageView iV) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0.1f);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        iV.setColorFilter(cf);
    }

    private void restoreOriginal(ImageView iV) {
        iV.clearColorFilter();
    }

    private void updateToggle() {
        if (toggle.isChecked()) {
            toggle.setText(lang.on());
        } else {
            toggle.setText(lang.off());
        }
    }
}