package com.example.jpushdemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    private static final String PREFS = "iot_prefs";

    public static void setDeviceID(Context context,String device_id){
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("device_id", device_id);
        editor.commit();
    }

    public static String getDeviceID(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREFS,Activity.MODE_PRIVATE);
        String device_id = pref.getString("device_id",null);//第二个参数为默认值
        return device_id;
    }
}
