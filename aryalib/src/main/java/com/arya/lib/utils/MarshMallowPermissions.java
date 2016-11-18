package com.arya.lib.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by user on 31/05/16.
 */
public class MarshMallowPermissions {

    public static final int CALENDAR_PERMISSION_REQUEST_CODE = 1;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 2;
    public static final int CONTACTS_PERMISSION_REQUEST_CODE = 3;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 4;
    public static final int RECORD_PERMISSION_REQUEST_CODE = 5;
    public static final int PHONE_PERMISSION_REQUEST_CODE = 6;
    public static final int SENSOR_PERMISSION_REQUEST_CODE = 7;
    public static final int SMS_PERMISSION_REQUEST_CODE = 8;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 9;
    Activity activity;

    public MarshMallowPermissions(Activity activity) {
        this.activity = activity;
    }


    public boolean checkPermissionForCalendar() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission_group.CALENDAR);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkPermissionForContacts() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission_group.CONTACTS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkPermissionForLocation() {
        int result1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result1 == PackageManager.PERMISSION_GRANTED || result2 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForRecord() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkPermissionForPhone() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkPermissionForSensors() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission_group.SENSORS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkPermissionForSms() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission_group.SMS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkPermissionForExternalStorage() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED || result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissionForCalendar() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission_group.CALENDAR)) {
            Util.showCenteredToast(activity, "Calendar permission needed to process. Please allow in App Settings for additional functionality.");
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission_group.CALENDAR}, CALENDAR_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            Util.showCenteredToast(activity, "Camera permission needed. Please allow in App Settings for additional functionality.");
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForContacts() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission_group.CONTACTS)) {
            Util.showCenteredToast(activity, "Contacts permission needed to access. Please allow in App Settings for additional functionality.");
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission_group.CONTACTS}, CONTACTS_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForLocation() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Util.showCenteredToast(activity, "Location permission needed. Please allow in App Settings for additional functionality.");
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

        }
    }

    public void requestPermissionForRecord() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
            Util.showCenteredToast(activity, "Microphone permission needed for recording. Please allow in App Settings for additional functionality.");
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForPhone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {
            Util.showCenteredToast(activity, "Camera permission needed. Please allow in App Settings for additional functionality.");
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, PHONE_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForSensor() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission_group.SENSORS)) {
            Util.showCenteredToast(activity, "Camera permission needed. Please allow in App Settings for additional functionality.");
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission_group.SENSORS}, SENSOR_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForSms() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS)) {
            Util.showCenteredToast(activity, "Camera permission needed. Please allow in App Settings for additional functionality.");
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Util.showCenteredToast(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.");
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }
}