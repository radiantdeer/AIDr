package com.aidr.aidr;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.aidr.aidr.Adapter.LocationMessageViewHolder;
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
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    public final static String chatHistoryFilename = "chat_history.json";
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");
    private final Author user = new Author("5005","You",null); // user
    private final Author system = new Author("8899","AIDr",null); // the system
    private final byte CONTENT_TYPE_DISEASE = 1;
    private final byte CONTENT_TYPE_LOCATIONS = 2;

    private final int REQUEST_MICROPHONE = 11;

    private MessagesListAdapter<Message> adapter; // Adapter for viewing messages in chatList
    private boolean speechMode = true; // Mode state
    private boolean sendMode = false;
    private MessageInput chatInput;
    private MessagesList chatList;
    private ImageButton attachFileBtn;
    private ImageButton switchModeBtn;
    private ImageButton speechInputBtn;

    private MessageHolders.ContentChecker<Message> contentChecker = new MessageHolders.ContentChecker<Message>() {

        @Override
        public boolean hasContentFor(Message message, byte type) {
            switch (type) {
                case CONTENT_TYPE_DISEASE:
                    return (message.getDetailId() != -1);
                case CONTENT_TYPE_LOCATIONS:
                    return message.isShowingLocations();
            }
            return false;
        }
    };


    private JSONArray messages;

    private int currDisease = -1;
    private boolean isDiagnoseOnProgress = false;
    private int suspectedDisease = -1;

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
        holderConfig.registerContentType(CONTENT_TYPE_LOCATIONS,LocationMessageViewHolder.class, R.layout.chat_bubble_location, OutMessageViewHolder.class, R.layout.chat_bubble_outgoing, contentChecker);

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
                String temp = input.toString();
                if (!temp.isEmpty()) {
                    sendMessage(temp);
                }
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

        chatList.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                chatList.smoothScrollToPosition(0);
            }
        });

        /* Listener when user inputs something in text box, the "voice-switch" button will change into a send button */
        chatInput.getInputEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    sendMode = true;
                    switchModeBtn.setImageResource(R.drawable.ic_send_black_24dp);
                } else {
                    sendMode = false;
                    switchModeBtn.setImageResource(R.drawable.ic_keyboard_voice_black_24dp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /* Speech-related stuff down below */
        final Message listenPlaceholder = new Message("Listening...","spch",user,new Date());
        final Message processingPlaceholder = new Message("Processing...","spch",user,new Date());

        final SpeechRecognizer sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());

        sr.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> arrResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                float[] confidencePoints = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);

                /* Finding best match */
                float maxVal = confidencePoints[0];
                int maxPos = 0;
                for (int i = 1; i < confidencePoints.length; i++) {
                    if (maxVal < confidencePoints[i]) {
                        maxVal = confidencePoints[i];
                        maxPos = i;
                    }
                }
                String bestMatch = arrResults.get(maxPos);
                if (!bestMatch.equals("")) {
                    sendMessage(bestMatch);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        speechInputBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sr.startListening(new Intent());
                        adapter.addToStart(listenPlaceholder,true);
                        return true;
                    case MotionEvent.ACTION_UP:
                        sr.stopListening();
                        adapter.delete(listenPlaceholder);
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
        boolean isDisease = false;
        boolean showLocation = false;
        try {
            id = (String) in.get("id");
            text = (String) in.get("text");
            author = convertAuthJSONtoAuthObj((JSONObject) in.get("author"));
            tstamp = sdf.parse((String) in.get("tstamp"));
            detailId = ((Number) in.get("detailId")).intValue();
            isDisease = in.getBoolean("isDisease");
            showLocation = in.getBoolean("showLocation");
        } catch (Exception e) {
            // You probably passed the wrong JSONObject
            author = new Author("0","ERR",null);
            tstamp = new Date();
        }
        if (showLocation) {
            return new Message(text,id,author,tstamp,showLocation);
        } else {
            return new Message(text,id,author,tstamp,detailId,isDisease);
        }

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

    /* Button handler for switchButton, because it also serves the purpose as a send button */
    public void sendOrVoiceButtonHandler(View view) {
        if (sendMode) {
            chatInput.getButton().callOnClick();
            sendMode = false;
            switchModeBtn.setImageResource(R.drawable.ic_keyboard_voice_black_24dp);
        } else {
            switchMode(view);
        }
    }

    /* Switch input mode */
    public void switchMode(View view) {
        speechMode = !speechMode;
        System.out.println(speechMode);
        if (speechMode) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_MICROPHONE);
                System.out.println("Requesting microphone access...");
                speechMode = !speechMode;
                System.out.println(speechMode);
            } else {
                chatInput.setVisibility(View.INVISIBLE);
                attachFileBtn.setVisibility(View.INVISIBLE);

                speechInputBtn.setVisibility(View.VISIBLE);
                switchModeBtn.setImageResource(R.drawable.ic_keyboard_black_24dp);
            }
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

    /* Simulate sending message */
    public void sendMessage(final String query) {
        Message msg = new Message(query,"lel",user, new Date());
        addMessage(msg, false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                processChat(query);
            }
        }, 2000);
    }

    /* Adds message to MessageList & dumps chat history */
    public void addMessage(Message m) {
        addMessage(m,true);
    }

    /* Adds message to MessageList. Set dumpChat to true to execute dumping chat history */
    public void addMessage(Message m, boolean dumpChat) {
        adapter.addToStart(m, true);
        try {
            messages.put(new JSONObject(m.toString()));
        } catch (Exception e) {
            // no need, guaranteed to work
            e.printStackTrace();
        }

        /* Save to file */
        if (dumpChat) {
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
                Message m;

                String forChecksOnly = query.toLowerCase();

                // If diagnosing is still undergoing...
                if (isDiagnoseOnProgress) {
                    if ((suspectedDisease == DiseaseDB.getDiseaseIdByNameIgnoreCase("cold")) && forChecksOnly.contains("yes")) {
                        m = new Message("Aright, based on your condition you've told me, you might have cold. Here's what I have about cold.","dgs",system,new Date(),suspectedDisease,true);
                        System.out.println(suspectedDisease);
                        isDiagnoseOnProgress = false;
                        currDisease = suspectedDisease;
                    } else if ((suspectedDisease == DiseaseDB.getDiseaseIdByNameIgnoreCase("cold")) && forChecksOnly.contains("no")){
                        suspectedDisease = DiseaseDB.getDiseaseIdByName("Common sore throat");
                        m = new Message("Aright, based on your condition you've told me, you might have common sore throat. Here's what I have about common sore throat.","dgs",system,new Date(),suspectedDisease, true);
                        isDiagnoseOnProgress = false;
                        currDisease = suspectedDisease;
                    } else  {
                        m = new Message("I didn't understand that","dgs",system,new Date());
                    }
                    addMessage(m);


                // One possibility of diagnosing
                } else if (forChecksOnly.contains("have") && forChecksOnly.contains("sore throat")) {
                    suspectedDisease = DiseaseDB.getDiseaseIdByNameIgnoreCase("cold");
                    m = new Message("Okay, do you experience fever/cold during the last week?","dgs",system,new Date());
                    addMessage(m);
                    isDiagnoseOnProgress = true;

                // Show hospital locations
                } else if ((forChecksOnly.contains("show") || forChecksOnly.contains("where")) && forChecksOnly.contains("nearest") && (forChecksOnly.contains("hospital"))) {
                    m = new Message("Here is the nearest hospitals","loc",system,new Date(),true);
                    addMessage(m);

                // Show drugs available to a disease
                } else if (forChecksOnly.contains("what") && (forChecksOnly.contains("drug") || forChecksOnly.contains("medicine")) && (forChecksOnly.contains("take")) && currDisease != -1) {
                    String disName = DiseaseDB.getDiseaseNameById(currDisease);
                    JSONArray temp = DiseaseDB.getDrugsByDiseaseId(currDisease);
                    if (temp.length() <= 0) {
                        m = new Message("I'm sorry, I currently do not know any drugs for that disease...", "drg", system, new Date());
                        addMessage(m);
                    } else {
                        m = new Message("Here are some drugs for alleviating " + disName + ". Any of these below should be suitable!", "drg", system, new Date());
                        addMessage(m);
                        for (int i = 0; i < temp.length(); i++) {
                            try {
                                int drugId = temp.getJSONObject(i).getInt("id");
                                m = new Message("", "drgMr", system, new Date(), drugId, false);
                                addMessage(m);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                // gimmick - reply back with "You're welcome!" when user says "Thanks!", or "Thank you!"
                } else if (forChecksOnly.contains("thanks") || (forChecksOnly.contains("thank") && forChecksOnly.contains("you"))) {
                    m = new Message("You're welcome!","thx",system,new Date());
                    addMessage(m);
                    currDisease = -1;

                // Search for a disease, or just mirror the user if no disease found
                } else if (forChecksOnly.contains("tell") && forChecksOnly.contains("about")) {
                    String diseaseKeyWord = forChecksOnly.substring(forChecksOnly.lastIndexOf("about ") + 6);
                    int detailId = DiseaseDB.getDiseaseIdByNameIgnoreCase(diseaseKeyWord);
                    if (detailId == -1) {
                        m = new Message("Sorry, I don't know about " + diseaseKeyWord + ".", "lel", system, new Date());
                        currDisease = -1;
                    } else {
                        m = new Message("Here's what I know about " + diseaseKeyWord + ".", "dis", system, new Date(), detailId, true);
                        currDisease = detailId;
                    }
                    addMessage(m);

                // Else, just mirrors user's message
                } else {
                    m = new Message("You sent : " + query, "lel", system, new Date());
                    addMessage(m);
                }
            }
        }, 1500);
    }
}
