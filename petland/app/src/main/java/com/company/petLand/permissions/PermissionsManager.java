package com.company.petLand.permissions;


import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;


public class PermissionsManager {


    public static boolean isAllPermissionsGranted(Activity activity , ArrayList<String> permissions){

        boolean isAllPermissionGranted = true;
        for (String permission : permissions) {
            int permissionResult = ContextCompat.checkSelfPermission(activity, permission);
            boolean isPermissionGranted = permissionResult == PackageManager.PERMISSION_GRANTED;
            isAllPermissionGranted = isAllPermissionGranted && isPermissionGranted;
        }

        return isAllPermissionGranted;
    }

    public static void requestPermissions(Activity activity , ArrayList<String> permissions , int permissionsRequestCode) {

        String[] permissionsStringArray = permissions.toArray(new String[permissions.size()]);

        ActivityCompat.requestPermissions(activity , permissionsStringArray , permissionsRequestCode);

    }

    public static void requestPermission(Activity activity , String permission , int permissionsRequestCode) {

        String[] permissionsStringArray = new String[]{permission};

        ActivityCompat.requestPermissions(activity , permissionsStringArray , permissionsRequestCode);

    }

    public static void requestPermissionsIfNeeded(Activity activity , ArrayList<String> permissions , int permissionsRequestCode){
        if(isAllPermissionsGranted(activity , permissions)){
            //MyToast.show("All com.myprojects.ChatMe.Permissions Granted Successfully");
        }
        else {

            requestPermissions(activity , permissions , permissionsRequestCode);
        }
    }

    public static boolean isPermissionGranted(Activity activity , String permission) {
        boolean isGranted = false;

        if (ContextCompat.checkSelfPermission(activity , permission) == PackageManager.PERMISSION_GRANTED) {
            isGranted = true;
        }
        return isGranted;
    }


}
