package com.openback.androidsampleapp;

import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import com.openback.OpenBack;

public class Application extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        initOpenBack(context);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void enableStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
    }

    public void initOpenBack(Context context) {
        try {
            OpenBack.start(new OpenBack.Config(context)
                    .setOpenBackAppCode("IYYTNHSYZA")
                    .setUserEmail("YOUR_EMAIL@youremail.com"));
        } catch (Exception e) {
            Log.d("CompanionApp", "Whoops", e);
        }
    }
}