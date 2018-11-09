package com.aidr.aidr.Model;

import android.view.View;

import com.stfalcon.chatkit.messages.MessagesListAdapter;

public class OutMessageViewHolder extends MessagesListAdapter.OutcomingMessageViewHolder<Message> {

    public OutMessageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
    }
}
