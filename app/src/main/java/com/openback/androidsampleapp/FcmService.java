package com.openback.androidsampleapp;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.openback.OpenBack;

public class FcmService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (!OpenBack.handleFcmMessage(getApplicationContext(), remoteMessage)) {
            // FCM Message was not handled by OpenBack
        }
    }
}