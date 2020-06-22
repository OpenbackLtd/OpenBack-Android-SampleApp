package com.openback.androidsampleapp;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.openback.OpenBack;

public class FcmService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e("FIREBASE", "New token: " + token);
        OpenBack.refreshFcmToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (!OpenBack.handleFcmMessage(remoteMessage.getData())) {
            // FCM Message was not handled by OpenBack
            Log.e("FIREBASE", "Not an OpenBack message");
        }
    }
}