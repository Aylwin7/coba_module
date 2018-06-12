package com.coba_module;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EXPackage implements ReactPackage {

   @Override
  public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
    return Collections.emptyList();
  }

  @Override
  public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
    List<NativeModule> modules = new ArrayList<>();
    modules.add(new IMEI(reactContext));
    modules.add(new wifi(reactContext));
    modules.add(new storage(reactContext));    
    modules.add(new FingerprintAndroid(reactContext));
    modules.add(new ClearCacheModule(reactContext));
    modules.add(new ExitApp(reactContext));
    return modules;
  }

}