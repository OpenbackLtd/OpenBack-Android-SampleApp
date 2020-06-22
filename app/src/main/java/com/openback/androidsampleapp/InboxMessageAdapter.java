package com.openback.androidsampleapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openback.model.AppInboxMessage;

import java.util.List;

public class InboxMessageAdapter extends RecyclerView.Adapter<InboxMessageAdapter.ViewHolder> {
    private List<AppInboxMessage> mInboxMessages;
    private LayoutInflater mInflater;
    private InboxMessageClickListener mClickListener;

    InboxMessageAdapter(Context context, List<AppInboxMessage> inboxMessages) {
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
        AppInboxMessage message = mInboxMessages.get(position);
        holder.inboxTitleView.setText(message.title);
        holder.inboxTitleView.setTypeface(null, message.read ? Typeface.NORMAL : Typeface.BOLD);
        holder.inboxContentView.setText(message.content);
        holder.inboxContentView.setTypeface(null, message.read ? Typeface.NORMAL : Typeface.BOLD);
        holder.inboxPayloadView.setText(message.payload);
        holder.inboxReadView.setImageResource(message.read ? R.drawable.baseline_mail_outline_24 : R.drawable.baseline_email_24);
        holder.inboxActionView.setVisibility(message.actionable ? View.VISIBLE : View.INVISIBLE);
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

    AppInboxMessage getInboxMessage(int id) {
        return mInboxMessages.get(id);
    }

    void setClickListener(InboxMessageClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface InboxMessageClickListener {
        void onInboxMessageClick(View view, int position);
    }
}
