package com.openback.androidsampleapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.openback.OpenBack;
import com.openback.OpenBackAppInbox;
import com.openback.OpenBackAppInboxMessage;

import java.util.ArrayList;

import static com.openback.OpenBackAppInbox.APP_INBOX_MESSAGE_ADDED;
import static com.openback.OpenBackAppInbox.APP_INBOX_MESSAGE_EXPIRED;
import static com.openback.OpenBackAppInbox.APP_INBOX_MESSAGE_READ;
import static com.openback.OpenBackAppInbox.OPENBACK_APP_INBOX;

public class InboxActivity extends AppCompatActivity implements InboxMessageAdapter.InboxMessageClickListener {

    private InboxMessageAdapter inboxMessageAdapter;
    private OpenBackAppInbox inbox;
    private ArrayList<OpenBackAppInboxMessage> messages = new ArrayList<>();

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String event = intent.getStringExtra("event");
            switch (event) {
                case APP_INBOX_MESSAGE_ADDED:
                case APP_INBOX_MESSAGE_READ:
                case APP_INBOX_MESSAGE_EXPIRED:
                    messages.clear();
                    messages.addAll(inbox.getAllMessages());
                    inboxMessageAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        inbox = OpenBack.appInbox(getApplicationContext());
        messages = inbox.getAllMessages();

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
                OpenBackAppInboxMessage message = inboxMessageAdapter.getInboxMessage(position);
                inbox.removeMessage(message);
                messages.remove(position);
                inboxMessageAdapter.notifyDataSetChanged();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageReceiver, new IntentFilter(OPENBACK_APP_INBOX));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onInboxMessageClick(View view, int position) {
        OpenBackAppInboxMessage message = inboxMessageAdapter.getInboxMessage(position);
        if (message.isActionable()) {
            inbox.executeMessage(message);
            inboxMessageAdapter.notifyDataSetChanged();
        }
    }
}
