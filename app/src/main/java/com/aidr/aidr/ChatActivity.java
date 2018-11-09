package com.aidr.aidr;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.aidr.aidr.Model.Author;
import com.aidr.aidr.Adapter.DiseaseMessageViewHolder;
import com.aidr.aidr.Model.Message;
import com.aidr.aidr.Adapter.OutMessageViewHolder;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    public final static String chatHistoryFilename = "chat_history.json";
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");
    private final Author user = new Author("5005","You",null); // user
    private final Author system = new Author("8899","AIDr",null); // the system
    private final byte CONTENT_TYPE_DISEASE = 1;

    private MessagesListAdapter<Message> adapter; // Adapter for viewing messages in chatList
    private boolean speechMode = true; // Mode state
    private MessageInput chatInput;
    private MessagesList chatList;
    private ImageButton attachFileBtn;
    private ImageButton switchModeBtn;
    private ImageButton speechInputBtn;

    private JSONArray messages;

    private MessageHolders.ContentChecker<Message> contentChecker = new MessageHolders.ContentChecker<Message>() {

        @Override
        public boolean hasContentFor(Message message, byte type) {
            switch (type) {
                case CONTENT_TYPE_DISEASE:
                    return (message.getDetailId() != -1);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        MessageHolders holderConfig = new MessageHolders();
        // Setting chat bubble default layout
        holderConfig.setIncomingTextLayout(R.layout.chat_bubble_incoming);
        holderConfig.setOutcomingTextLayout(R.layout.chat_bubble_outgoing);
        // Setting chat bubble layout for specific chat content type
        holderConfig.registerContentType(CONTENT_TYPE_DISEASE, DiseaseMessageViewHolder.class, R.layout.chat_bubble_disease, OutMessageViewHolder.class, R.layout.chat_bubble_outgoing, contentChecker);
        // Linking message adapter to it's MessageList
        adapter = new MessagesListAdapter<>(user.getId(), holderConfig,null);
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
                Message msg = new Message(input.toString(),"lel",user, new Date());
                adapter.addToStart(msg, true);
                try {
                    messages.put(new JSONObject(msg.toString()));
                } catch (Exception e) {
                    // no need, guaranteed to work
                    e.printStackTrace();
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        processChat(input.toString());
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

        // Get desired input mode
        int buttonType = getIntent().getIntExtra(AIDrChat.EXTRA_MSG_CHAT,1);

        /* Notice that we actually assign inverted values here.
           This is because speechMode will be inverted again when invoking switchMode. */
        speechMode = (buttonType == 1);

        /* Set input mode */
        switchMode(switchModeBtn);

        /* Loads chat history */
        loadChatHistory();
    }

    private Message convertMsgJSONtoMsgObj(JSONObject in) {
        String id = "";
        String text = "";
        Author author = null;
        Date tstamp = null;
        int detailId = -1;
        try {
            id = (String) in.get("id");
            text = (String) in.get("text");
            author = convertAuthJSONtoAuthObj((JSONObject) in.get("author"));
            tstamp = sdf.parse((String) in.get("tstamp"));
            detailId = ((Number) in.get("detailId")).intValue();
        } catch (Exception e) {
            // You probably passed the wrong JSONObject
            author = new Author("0","ERR",null);
            tstamp = new Date();
        }
        return new Message(text,id,author,tstamp,detailId);
    }

    private Author convertAuthJSONtoAuthObj(JSONObject in) {
        String id = "";
        String name = "";
        String ava = null;
        try {
            id = (String) in.get("id");
            name = (String) in.get("name");
            ava = (String) in.get("avatar");
        } catch (Exception e) {
            // You probably passed the wrong JSONObject
        }
        return new Author(id,name,ava);
    }

    /* Loads chat history from file
    *  Will load 10 most recent chat from file */
    private void loadChatHistory() {
        loadChatHistory(10);
    }

    /* Loads chat history from file
    *  Will load nMessage most recent chat from file */
    private void loadChatHistory(int nMessage) {
        /* Check reminder file. If not exists, create an empty file */
        File file = new File(getFilesDir(), chatHistoryFilename);
        if (!file.exists()) {
            try {
                FileOutputStream ostream = openFileOutput(chatHistoryFilename, Context.MODE_PRIVATE);
                String toWrite = "[]";
                ostream.write(toWrite.getBytes(Charset.defaultCharset()));
                ostream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            file = new File(getFilesDir(), chatHistoryFilename);
        }

        /* Now loads & reads the file */
        FileInputStream istream;
        boolean success = true;
        try {
            istream = openFileInput(chatHistoryFilename);
        } catch (FileNotFoundException fnfe) {
            // should not happen
            istream = null;
            fnfe.printStackTrace();
        }

        if (istream != null) {
            byte[] buffer = new byte[(int)file.length()];
            try {
                istream.read(buffer);
            } catch (Exception e) {
                success = false;
                e.printStackTrace();
            }

            if (success) {
                String inputStr = new String(buffer, Charset.defaultCharset());
                try {
                    messages = new JSONArray(inputStr);
                    int startPoint = messages.length() - nMessage;
                    if (startPoint < 0) {
                        startPoint = 0;
                    }
                    for (int i = startPoint; i < messages.length();i++) {
                        Message temp = convertMsgJSONtoMsgObj(messages.getJSONObject(i));
                        adapter.addToStart(temp, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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

    /* Process message from user */
    public void processChat(final String query) {
        /* Is-typing "effect" --> this will change if a better method (in UI perspective) is found */
        final Message loading = new Message("AIDr is typing...","0",system, new Date());
        adapter.addToStart(loading, true);

        /* Send actual data */
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.delete(loading);
                int detailId = DiseaseDB.getDiseaseIdByName(query);
                Message m;
                if (detailId == -1) {
                    m = new Message("You sent : " + query, "lel", system, new Date(), detailId);
                } else {
                    m = new Message("You asked for " + query + ". Here's what I know about " + query + ".", "dis", system, new Date(), detailId);
                }
                adapter.addToStart(m, true);
                try {
                    messages.put(new JSONObject(m.toString()));
                } catch (Exception e) {
                    // no need, guaranteed to work
                    e.printStackTrace();
                }

                /* Save to file */
                FileOutputStream ostream = null;
                try {
                    ostream = openFileOutput(chatHistoryFilename, Context.MODE_PRIVATE);
                } catch (Exception e) {
                    // should not happen, file is guaranteed to be available
                    e.printStackTrace();
                }

                if (ostream != null) {
                    try {
                        ostream.write(messages.toString().getBytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 1500);
    }
}
