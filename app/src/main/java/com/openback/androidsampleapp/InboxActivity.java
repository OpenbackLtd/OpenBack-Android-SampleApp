package com.openback.androidsampleapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.openback.OpenBack;
import com.openback.model.AppInboxMessage;

import java.util.List;

public class InboxActivity extends AppCompatActivity implements InboxMessageAdapter.InboxMessageClickListener {

    private InboxMessageAdapter inboxMessageAdapter;
    private List<AppInboxMessage> messages;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String event = intent.getStringExtra(OpenBack.APP_INBOX_EVENT_EXTRA);
            if (event != null) {
                switch (event) {
                    case OpenBack.APP_INBOX_MESSAGE_ADDED:
                    case OpenBack.APP_INBOX_MESSAGE_READ:
                    case OpenBack.APP_INBOX_MESSAGE_EXPIRED:
                        messages = OpenBack.getAppInboxMessages();
                        inboxMessageAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        setTitle("OpenBack App Inbox");

        messages = OpenBack.getAppInboxMessages();

        RecyclerView recyclerView = findViewById(R.id.inboxMessages);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        inboxMessageAdapter = new InboxMessageAdapter(this, messages);
        inboxMessageAdapter.setClickListener(this);
        recyclerView.setAdapter(inboxMessageAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                AppInboxMessage message = inboxMessageAdapter.getInboxMessage(position);
                OpenBack.removeAppInboxMessage(message);
                messages.remove(position);
                inboxMessageAdapter.notifyDataSetChanged();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageReceiver, new IntentFilter(OpenBack.APP_INBOX_ACTION));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onInboxMessageClick(View view, int position) {
        AppInboxMessage message = inboxMessageAdapter.getInboxMessage(position);
        if (message.actionable) {
            OpenBack.executeAppInboxMessage(message);
        } else {
            OpenBack.markAppInboxMessageAsRead(message);
        }
        inboxMessageAdapter.notifyDataSetChanged();
    }
}
