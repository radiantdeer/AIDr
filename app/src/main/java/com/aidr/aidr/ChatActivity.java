package com.aidr.aidr;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.aidr.aidr.Model.Author;
import com.aidr.aidr.Model.Message;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private String userid = "5005";
    private String name = "You";
    private Author system = new Author("8899","AIDr",null);
    private MessagesListAdapter<Message> adapter;
    private boolean speechMode = true;
    private MessageInput chatInput;
    private MessagesList chatList;
    private ImageButton attachFileBtn;
    private ImageButton switchModeBtn;
    private ImageButton speechInputBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        adapter = new MessagesListAdapter<>(userid,null);
        chatList = ((MessagesList) findViewById(R.id.messagesList));
        chatList.setAdapter(adapter);

        /* Linking it's output representative to logic representation */
        chatInput = (MessageInput) findViewById(R.id.input);
        attachFileBtn = (ImageButton) findViewById(R.id.attachButton);
        switchModeBtn = (ImageButton) findViewById(R.id.switchModeButton);
        speechInputBtn = (ImageButton) findViewById(R.id.talkButton);

        /* Submit listener */
        chatInput.setInputListener(new MessageInput.InputListener() {
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
        chatInput.getInputEditText().setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View view, int keyCode, KeyEvent ke) {
                if ((ke.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    chatInput.getButton().callOnClick();
                    return true;
                }
                return false;
            }
        });

        int buttonType = getIntent().getIntExtra(AIDrChat.EXTRA_MSG_CHAT,1);

        /* Notice that we actually assign inverted values here.
           This is because speechMode will be inverted again when invoking switchMode. */
        speechMode = (buttonType == 1);

        /* Set input mode */
        switchMode(switchModeBtn);
    }

    public void switchMode(View view) {
        speechMode = !speechMode;
        if (speechMode) {
            chatInput.setVisibility(View.INVISIBLE);
            attachFileBtn.setVisibility(View.INVISIBLE);

            speechInputBtn.setVisibility(View.VISIBLE);
            switchModeBtn.setImageResource(R.drawable.ic_keyboard_black_24dp);
        } else {
            speechInputBtn.setVisibility(View.INVISIBLE);
            switchModeBtn.setImageResource(R.drawable.ic_keyboard_voice_black_24dp);

            chatInput.setVisibility(View.VISIBLE);
            attachFileBtn.setVisibility(View.VISIBLE);
        }
    }

    /* close this activity -> return to main activity (which is AIDrChat) */
    public void closeChat(View view) {
        finish();
    }

    /* Author data structure - represents the sender of a message */


    /* Message data structure - represents a message */


    /* Process message from user */
    public void respondChat(final String query) {
        /* Is-typing "effect" */
        final Message loading = new Message("AIDr is typing...","0",system, new Date());
        adapter.addToStart(loading, true);

        /* Send actual data */
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.delete(loading);
                Message m = new Message("You sent : " + query, "1", system, new Date());
                adapter.addToStart(m, true);
            }
        }, 1500);
    }
}
