package com.openback.androidsampleapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.openback.OpenBack;
import com.openback.OpenBackException;
import com.openback.UserInfoExtra;

public class MainActivity extends AppCompatActivity {

    TextInputEditText firstName;
    TextInputEditText lastName;
    TextInputEditText email;
    TextInputEditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = getApplicationContext();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        // Use this line to check the current OpenBack SDK version
        TextView sdkVersion = findViewById(R.id.sdkVersion);
        sdkVersion.setText("OpenBack SDK Version is " + OpenBack.getSdkVersion());

        // Buttons for setting Custom Trigger values. These values can be set to String, int and float variables.
        // OpenBack typically supports up to 10 custom values, if you need more please discuss with OpenBack or email integrations@openback.com
        findViewById(R.id.customButton1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenBack.setCustomTrigger(context, OpenBack.CUSTOM_TRIGGER_1, "Bob");
                Toast.makeText(context, "Custom Trigger 1: String - Bob", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.customButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenBack.setCustomTrigger(context, OpenBack.CUSTOM_TRIGGER_2, 42);
                Toast.makeText(context, "Custom Trigger 2: int - 42", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.customButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenBack.setCustomTrigger(context, OpenBack.CUSTOM_TRIGGER_3, 1.12f);
                Toast.makeText(context, "Custom Trigger 3: Float - 1.12f", Toast.LENGTH_SHORT).show();
            }
        });

        // Button to instantly call checkCampaigns. This will immediately check for any matching triggers on your user's device.
        // Although it is highly recommended to let the OpenBack library decide when to check the campaigns, you can force it right away.
        findViewById(R.id.checkCampaigns).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenBack.checkCampaignsNow(context);
            }
        });

        // This button takes in the user info and saves the details to UserInfoExtra.
        // All extra user info fields can be found at https://docs.openback.com/Android%20Library%20Integration/#openback-library-api
        // These must be added to the userInfoExtra object and updated through OpenBack.update()
        findViewById(R.id.saveUserInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfoExtra userInfoExtra = new UserInfoExtra();
                userInfoExtra.OptInUpdates = "true";
                userInfoExtra.FirstName = firstName.getEditableText().toString();
                userInfoExtra.Surname = lastName.getEditableText().toString();
                try {
                    OpenBack.update(new OpenBack.Config(context)
                            .setExtraUserInfo(userInfoExtra)
                            .setUserEmail(email.getEditableText().toString())
                            .setUserMsisdn(phone.getEditableText().toString()));
                } catch (OpenBackException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.appInbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InboxActivity.class);
                startActivity(intent);
            }
        });
    }
}
