 
 package com.coba_module;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.text.format.Formatter;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import java.util.HashMap;
import java.util.Map;
 
public class wifi extends ReactContextBaseJavaModule {

    ReactApplicationContext reactContext;
    WifiInfo wifiInfo;
  
   public wifi(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "wifi";
    }

    // private WifiInfo getWifiInfo() {
    //     if (this.wifiInfo == null) {
       
    //     }
    //     return this.wifiInfo;
    // }

  @ReactMethod
  public void getIpAddress(Callback p) {
    WifiManager manager = (WifiManager)  this.reactContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    this.wifiInfo = manager.getConnectionInfo();
    String ipAddress =    Formatter.formatIpAddress( this.wifiInfo.getIpAddress());
    p.invoke(ipAddress);
}

}