package com.example.self_chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        MessageRecyclerUtils.SendClickCallback, MessageRecyclerUtils.MessageClickCallback {

    private static final String MESSAGE_VIEW_KEY = "messages_view";
    private static final String EMPTY_STR = "";
    private static final String EMPTY_MESSAGE_ERR = "You can't send an empty message, oh silly!";
    private final static String SP_ALL_MESSAGES = "self.chat.messages";

    private MessageRecyclerUtils.MessageAdapter adapter
            = new MessageRecyclerUtils.MessageAdapter();

    private ArrayList<Message> messages = new ArrayList<>();
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.message_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(adapter);
        adapter.callback = this;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String messagesListGson = sp.getString(SP_ALL_MESSAGES, null);
        gson = new Gson();

        if (savedInstanceState != null) {
            messages = savedInstanceState.getParcelableArrayList(MESSAGE_VIEW_KEY);
        } else if (messagesListGson != null) {
            messages = gson.fromJson(messagesListGson,
                    new TypeToken<ArrayList<Message>>(){}.getType());
        }

        adapter.submitList(messages);

        final EditText messageInput = findViewById(R.id.message_input);
        Button sendButton = findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable message = messageInput.getText();
                String msg_string = message.toString();
                message.clear();
                if (msg_string.equals(EMPTY_STR)) {
                    Toast.makeText(MainActivity.this, EMPTY_MESSAGE_ERR,
                            Toast.LENGTH_SHORT).show();
                } else {
                    onSendClick(new Message(msg_string));
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MESSAGE_VIEW_KEY, messages);
    }

    @Override
    public void onSendClick(Message msg) {
        ArrayList<Message> messagesCopy = new ArrayList<>(this.messages);
        messagesCopy.add(msg);
        this.messages = messagesCopy;
        this.adapter.submitList(this.messages);
        this.saveMessages();
    }

    @Override
    public void onMessageClick(Message msg) {
        ArrayList<Message> messagesCopy = new ArrayList<>(this.messages);
        messagesCopy.remove(msg);
        this.messages = messagesCopy;
        this.adapter.submitList(this.messages);
        this.saveMessages();
    }

    private void saveMessages() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SP_ALL_MESSAGES, gson.toJson(this.messages));
        editor.apply();
    }
}
