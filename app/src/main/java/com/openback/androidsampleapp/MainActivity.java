package com.openback.androidsampleapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import com.openback.OpenBack;
import com.openback.OpenBackAppInbox;
import com.openback.OpenBackAppInboxMessage;
import com.openback.OpenBackException;
import com.openback.UserInfoExtra;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.openback.OpenBackAppInbox.APP_INBOX_MESSAGE_ADDED;
import static com.openback.OpenBackAppInbox.APP_INBOX_MESSAGE_EXPIRED;
import static com.openback.OpenBackAppInbox.APP_INBOX_MESSAGE_READ;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText firstName;
    TextInputEditText lastName;
    TextInputEditText email;
    TextInputEditText phone;
    ImageView eventIcon;
    ArrayList<OpenBackAppInboxMessage> messages = new ArrayList<>();

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
        eventIcon = findViewById(R.id.eventIcon);
        eventIcon.setOnClickListener(this);

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

    public void checkInbox() {
        OpenBackAppInbox inbox = OpenBack.appInbox(getApplicationContext());
        messages = inbox.getAllMessages();
        if (messages.size() > 0) {
            String payload = messages.get(0).getPayload();
            setImage(payload);
        }
    }

    // When the image is clicked, an event is triggered.
    @Override
    public void onClick(View view) {
        if (view == eventIcon) {
            OpenBack.triggerEvent(getApplicationContext(), "ImageClicked", 0);
            OpenBack.checkCampaignsNow(getApplicationContext());
            Toast.makeText(getApplicationContext(), "The event _ImageClicked_ has been triggered.", Toast.LENGTH_SHORT).show();
        }
    }

    // This receiver updates whenever a new message is received to the inbox
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String event = intent.getStringExtra("event");
            switch (event) {
                case APP_INBOX_MESSAGE_ADDED:
                case APP_INBOX_MESSAGE_READ:
                case APP_INBOX_MESSAGE_EXPIRED:
                    OpenBackAppInbox inbox = OpenBack.appInbox(getApplicationContext());
                    messages = inbox.getAllMessages();
                    checkInbox();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    // This method sets the eventIcon image to whatever image is linked in the App Inbox payload
    public void setImage(String string) {
        Picasso.get().load(string).into(eventIcon);
    }
}
