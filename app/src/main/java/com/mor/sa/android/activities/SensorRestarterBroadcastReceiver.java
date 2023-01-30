package com.mor.sa.android.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.checker.sa.android.data.BasicLog;
import com.checker.sa.android.helper.Constants;

import static android.content.Context.MODE_PRIVATE;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {
 @Override
    public void onReceive(Context context, Intent intent) {

     Intent broadcastIntent = new Intent(context, SensorRestarterBroadcastReceiver.class);
     SharedPreferences myPrefs = context.getSharedPreferences("pref", MODE_PRIVATE);
     SplashScreen.addServiceLog(new BasicLog(
             myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
             myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),"AGAIN Starting service!",""));

     Log.i(SensorRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        context.startService(new Intent(context, comService.class));
    }
}