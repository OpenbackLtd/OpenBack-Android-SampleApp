package com.openback.androidsampleapp;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.openback.OpenBackAppInboxMessage;

import java.util.ArrayList;

public class InboxMessageAdapter extends RecyclerView.Adapter<InboxMessageAdapter.ViewHolder> {
    private ArrayList<OpenBackAppInboxMessage> mInboxMessages;
    private LayoutInflater mInflater;
    private InboxMessageClickListener mClickListener;

    InboxMessageAdapter(Context context, ArrayList<OpenBackAppInboxMessage> inboxMessages) {
        this.mInflater = LayoutInflater.from(context);
        this.mInboxMessages = inboxMessages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.inbox_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OpenBackAppInboxMessage message = mInboxMessages.get(position);
        holder.inboxTitleView.setText(message.getTitle());
        holder.inboxTitleView.setTypeface(null, message.isRead() ? Typeface.NORMAL : Typeface.BOLD);
        holder.inboxContentView.setText(message.getContent());
        holder.inboxContentView.setTypeface(null, message.isRead() ? Typeface.NORMAL : Typeface.BOLD);
        holder.inboxPayloadView.setText(message.getPayload());
        holder.inboxReadView.setImageResource(message.isRead() ? R.drawable.baseline_mail_outline_24 : R.drawable.baseline_email_24);
        holder.inboxActionView.setVisibility(message.isActionable() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mInboxMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView inboxTitleView;
        TextView inboxContentView;
        TextView inboxPayloadView;
        ImageView inboxReadView;
        ImageView inboxActionView;

        ViewHolder(View itemView) {
            super(itemView);
            inboxTitleView = itemView.findViewById(R.id.inboxMessageTitle);
            inboxContentView = itemView.findViewById(R.id.inboxMessageContent);
            inboxPayloadView = itemView.findViewById(R.id.inboxMessagePayload);
            inboxReadView = itemView.findViewById(R.id.inboxMessageRead);
            inboxActionView = itemView.findViewById(R.id.inboxMessageActionable);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onInboxMessageClick(view, getAdapterPosition());
            }
        }
    }

    OpenBackAppInboxMessage getInboxMessage(int id) {
        return mInboxMessages.get(id);
    }

    void setClickListener(InboxMessageClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface InboxMessageClickListener {
        void onInboxMessageClick(View view, int position);
    }
}
