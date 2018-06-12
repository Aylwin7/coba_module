package com.coba_module;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import java.util.HashMap;
import java.util.Map;

public class IMEI extends ReactContextBaseJavaModule {

    ReactApplicationContext reactContext;

    public IMEI(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }
    @Override
    public String getName() {
        return "IMEI";
    }

    // @Override
    // public Map<String, Object> getConstants() {
    //     final Map<String, Object> constants = new HashMap<>();

    //     TelephonyManager tm = (TelephonyManager) this.reactContext.getSystemService(Context.TELEPHONY_SERVICE);
    //     String imei = tm.getDeviceId().trim();
    //     if (imei.isEmpty()) {
    //         throw new RuntimeException("Failed to read IMEI (imei is empty!)");
    //     }
    //     constants.put("imei", imei);

    //     return constants;
    // }

    @ReactMethod
    public void getIMEI(Callback x){
        // String IMEI ="1234556";
        TelephonyManager tm = (TelephonyManager) 
        this.reactContext.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = tm.getDeviceId().trim();
            // IMEI='NULL'
        
        if (IMEI.isEmpty()) {
            IMEI="NUL";
            throw new RuntimeException("Failed to read IMEI (imei is empty!)");
        }
        x.invoke(IMEI);
    }

}