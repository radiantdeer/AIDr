package com.aidr.aidr;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    public String userid = "5005";
    public String name = "You";
    private Author system = new Author("8899","AIDr",null);
    public MessagesListAdapter<Message> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        adapter = new MessagesListAdapter<>(userid,null);
        ((MessagesList) findViewById(R.id.messagesList)).setAdapter(adapter);

        final MessageInput mi = (MessageInput) findViewById(R.id.input);

        /* Submit listener */
        mi.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(final CharSequence input) {
                Message msg = new Message(input.toString(),"lel",new Author(userid,name,null), new Date());
                adapter.addToStart(msg, true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        respondChat(input.toString());
                    }
                }, 2000);
                return true;
            }
        });

        /* Invoke submit on enter */
        mi.getInputEditText().setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View view, int keyCode, KeyEvent ke) {
                if ((ke.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mi.getButton().callOnClick();
                    return true;
                }
                return false;
            }
        });

        /*
        cv = (ChatView) findViewById(R.id.chat_view);
        cv.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(final ChatMessage cm) {
                // do nothing, automatically adds msg

                return true;
            }
        });
        */
    }

    public void closeChat(View view) {
        finish();
    }

    private class Author implements IUser {

        private String id;
        private String name;
        private String avatar;

        public Author(String id, String name, String ava) {
            this.id = id;
            this.name = name;
            this.avatar = ava;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getAvatar() {
            return avatar;
        }
    }

    private class Message implements IMessage {

        private String text;
        private String id;
        private Author author;
        private Date tstamp;

        public Message(String text, String id, Author author, Date tstamp) {
            this.text = text;
            this.id = id;
            this.author = author;
            this.tstamp = tstamp;
        }

        @Override
        public String getId() {
           return id;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public Author getUser() {
            return author;
        }

        @Override
        public Date getCreatedAt() {
            return tstamp;
        }

    }

    public void respondChat(String query) {
        Message m = new Message("You sent : " + query, "1", system, new Date());
        adapter.addToStart(m, true);
    }
}
