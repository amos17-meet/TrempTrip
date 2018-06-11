package com.example.student.trempme;

import android.content.Context;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.util.Log;

import java.util.Locale;

public class Helper {

    public Helper(){

    }

    public static void setDefaultLanguage(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    public static boolean isLocationServicesAvailable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)&&!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //All location services are disabled
            Log.w("is location", "false");
            return false;

        }
        return true;
    }
}
