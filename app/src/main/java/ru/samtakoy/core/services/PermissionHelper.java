package ru.samtakoy.core.services;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

public class PermissionHelper {


    public  boolean isStoragePermissionGranted(AppCompatActivity a) {

        if (Build.VERSION.SDK_INT >= 23) {
            if (a.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                a.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

}
