package com.coba_module;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.File;

import android.os.AsyncTask;


public class ClearCacheModule extends ReactContextBaseJavaModule {

    static public ClearCacheModule myclearCacheModule;

    public ClearCacheModule(ReactApplicationContext reactContext) {
        super(reactContext);
        myclearCacheModule = this;
    }

    @Override
    public String getName() {
        return "ClearCacheModule";
    }

    @ReactMethod
    public void getAppCacheSize(Callback callback) {

        long fileSize = 0;
        File filesDir = getReactApplicationContext().getFilesDir();// /data/data/package_name/files
        File cacheDir = getReactApplicationContext().getCacheDir();// /data/data/package_name/cache
        fileSize += getDirSize(filesDir);
        fileSize += getDirSize(cacheDir);
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            File externalCacheDir = getExternalCacheDir(getReactApplicationContext());//"<sdcard>/Android/data/<package_name>/cache/"
            fileSize += getDirSize(externalCacheDir);
        }
        if (fileSize > 0) {
            String strFileSize = formatFileSize(fileSize);
            String unit = formatFileSizeName(fileSize);
            callback.invoke(strFileSize, unit);
        } else {
            WritableMap params = Arguments.createMap();
            callback.invoke("0", "B");
        }
    }

    @ReactMethod
    public void clearAppCache(Callback callback) {
        ClearCacheAsyncTask asyncTask = new ClearCacheAsyncTask(myclearCacheModule, callback);
        asyncTask.execute(10);
    }


    private long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); 
            }
        }
        return dirSize;
    }

    private boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    private File getExternalCacheDir(Context context) {

        // return context.getExternalCacheDir(); API level 8

        // e.g. "<sdcard>/Android/data/<package_name>/cache/"

        return context.getExternalCacheDir();
    }


    private String formatFileSizeName(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = "B";
        } else if (fileS < 1048576) {
            fileSizeString = "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = "MB";
        } else {
            fileSizeString = "G";
        }
        return fileSizeString;
    }



    private String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS);
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024);
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576);
        } else {
            fileSizeString = df.format((double) fileS / 1073741824);
        }
        return fileSizeString;
    }

//    private String formatFileSize(long fileS) {
//        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
//        String fileSizeString = "";
//        if (fileS < 1024) {
//            fileSizeString = df.format((double) fileS) + "B";
//        } else if (fileS < 1048576) {
//            fileSizeString = df.format((double) fileS / 1024) + "KB";
//        } else if (fileS < 1073741824) {
//            fileSizeString = df.format((double) fileS / 1048576) + "MB";
//        } else {
//            fileSizeString = df.format((double) fileS / 1073741824) + "G";
//        }
//        return fileSizeString;
//    }

    public void clearCache() {

        getReactApplicationContext().deleteDatabase("webview.db");
        getReactApplicationContext().deleteDatabase("webview.db-shm");
        getReactApplicationContext().deleteDatabase("webview.db-wal");
        getReactApplicationContext().deleteDatabase("webviewCache.db");
        getReactApplicationContext().deleteDatabase("webviewCache.db-shm");
        getReactApplicationContext().deleteDatabase("webviewCache.db-wal");
        clearCacheFolder(getReactApplicationContext().getFilesDir(), System.currentTimeMillis());
        clearCacheFolder(getReactApplicationContext().getCacheDir(), System.currentTimeMillis());
        
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            clearCacheFolder(getExternalCacheDir(getReactApplicationContext()), System.currentTimeMillis());
        }

    }

    private int clearCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

}