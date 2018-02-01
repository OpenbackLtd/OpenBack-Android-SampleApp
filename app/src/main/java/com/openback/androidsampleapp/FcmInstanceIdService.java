package com.openback.androidsampleapp;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FcmInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        try {
            String token = FirebaseInstanceId.getInstance().getToken();
            Log.d("TAG", "Device Firebase token: " + token);
        } catch (Exception e) {
            Log.e("TAS", e.getLocalizedMessage());
        }
    }
}
