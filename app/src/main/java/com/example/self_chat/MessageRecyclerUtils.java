package com.example.self_chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class MessageRecyclerUtils {
    static class MessageCallback extends DiffUtil.ItemCallback<Message> {

        @Override
        public boolean areItemsTheSame(@NonNull Message m1, @NonNull Message m2) {
            return m1.getContent().equals(m2.getContent());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message m1, @NonNull Message m2) {
            return m1.equals(m2);
        }
    }


    interface SendClickCallback {
        void onSendClick(Message msg);
    }

    interface MessageClickCallback {
        void onMessageClick(Message msg);
    }

    static class MessageAdapter extends ListAdapter<Message, MessageHolder> {

        MessageAdapter() {
            super(new MessageCallback());
        }

        MessageClickCallback callback;

        @NonNull @Override
        public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int itemType) {
            final Context context = parent.getContext();

            View itemView = LayoutInflater.from(context)
                            .inflate(R.layout.item_one_message, parent, false);

            final MessageHolder holder = new MessageHolder(itemView);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.delete_message_title)
                            .setMessage(R.string.delete_message_question)
                            .setPositiveButton(android.R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Message message = getItem(holder.getAdapterPosition());
                                    if (callback != null)
                                        callback.onMessageClick(message);
                                }
                            })

                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return true;
                }
            });
            return holder;

        }

        @Override
        public void onBindViewHolder(@NonNull MessageHolder personHolder, int position) {
            Message msg = getItem(position);
            personHolder.text.setText(msg.getContent());
        }
    }

    static class MessageHolder extends RecyclerView.ViewHolder {
        final TextView text;

        MessageHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.message_text);
        }
    }
}
