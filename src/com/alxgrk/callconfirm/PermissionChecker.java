/*
 * Created on 20.02.2016
 *
 * author Alex
 */
package com.alxgrk.callconfirm;

import java.util.EnumSet;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class PermissionChecker {

	private static final String TAG = PermissionChecker.class.getSimpleName();
	
    public static final int PERMISSION_REQUEST_CODE = 14851;

    public enum Permission {
        PROCESS_OUTGOING_CALLS(Manifest.permission.PROCESS_OUTGOING_CALLS), CALL_PHONE(
                Manifest.permission.CALL_PHONE);

        private String permission;

        private Permission(String permission) {
            this.permission = permission;
        }

        @Override
        public String toString() {
            return permission;
        }
    }

    private Activity activity;

    public PermissionChecker(Activity activity) {
        this.activity = activity;
    }

    public void askUserForMissingPermissions() {
        askUserForPermissions(getMissingPermissions());
    }

    private void askUserForPermissions(EnumSet<Permission> permissionSet) {
        if (!permissionSet.isEmpty()) {
            String[] array = new String[permissionSet.size()];
            int count = 0;
            for (Permission permission : permissionSet) {
                array[count++] = permission.toString();
            }

            ActivityCompat.requestPermissions(activity, array, PERMISSION_REQUEST_CODE);
        }
    }

    private EnumSet<Permission> getMissingPermissions() {
        EnumSet<Permission> missingPermissions = EnumSet.noneOf(Permission.class);

        if (!hasPermissionCallPhone())
            missingPermissions.add(Permission.CALL_PHONE);
        if (!hasPermissionProcessOutgoingCalls())
            missingPermissions.add(Permission.PROCESS_OUTGOING_CALLS);

        return missingPermissions;
    }

    public boolean hasPermissions() {
        if (hasPermissionProcessOutgoingCalls() && hasPermissionCallPhone()) {
            Log.d(TAG, "permissions granted");
            return true;
        } else {
            Log.d(TAG, "permissions denied");
            return false;
        }
    }

    private boolean hasPermissionProcessOutgoingCalls() {
        boolean hasPermission = hasPermission(Permission.PROCESS_OUTGOING_CALLS);
        Log.d("MainActivity", "has permission PROCESS_OUTGOING_CALLS: " + hasPermission);
        return hasPermission;
    }

    private boolean hasPermissionCallPhone() {
        boolean hasPermission = hasPermission(Permission.CALL_PHONE);
        Log.d("MainActivity", "has permission CALL_PHONE: " + hasPermission);
        return hasPermission;
    }

    private boolean hasPermission(Permission permission) {
        int code = ContextCompat.checkSelfPermission(activity, permission.toString());
        return code == PackageManager.PERMISSION_GRANTED;
    }
}