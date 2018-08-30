package com.openback.androidsampleapp;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.openback.OpenBack;

public class FcmService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (!OpenBack.handleFcmMessage(getApplicationContext(), remoteMessage)) {
            // FCM Message was not handled by OpenBack
        }
    }
}