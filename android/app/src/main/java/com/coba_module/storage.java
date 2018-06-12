package com.coba_module;

import android.content.Context;


import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import java.util.HashMap;
import java.util.Map;

public class storage extends ReactContextBaseJavaModule {

    ReactApplicationContext reactContext;

    public storage(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }
    @Override
    public String getName() {
        return "storage";
    }


    @ReactMethod
        public void getTotalDiskCapacity(Callback x) {
            try {
                StatFs root = new StatFs(Environment.getRootDirectory().getAbsolutePath());
                x.invoke(root.getBlockCount() * root.getBlockSize());
                // x.invoke(root.getBlockCountLong() * root.getBlockSize());
             } catch (Exception e) {
                e.printStackTrace();
            }
        }

  @ReactMethod
        public void getFreeDiskStorage(Callback x) {
            try {
                StatFs external = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
                x.invoke(external.getAvailableBlocks() * external.getBlockSize());
                // x.invoke(external.getAvailableBlocksLong() * external.getBlockSize());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}