package com.aidr.aidr.Adapter;

import android.view.View;

import com.aidr.aidr.Model.Message;
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
