package com.alxgrk.callconfirm;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        final PermissionChecker permissionChecker = new PermissionChecker(this);
        boolean hasPermissions = ensurePermissionsGranted(permissionChecker);

        toggle = (ToggleButton) findViewById(R.id.toggleActivation);
        toggle.setChecked(hasPermissions);
        toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "toggle state changed");
                if (isChecked) {
                    ensurePermissionsGranted(permissionChecker);
                }
            }
        });
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
}