package com.example.self_chat;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;

public class SelfChatApp extends Application {

    public final static String FB_MEESAGES_KEY = "fb_messages";
    public final static String SP_ALL_MESSAGES = "self.chat.messages";

    private ArrayList<Message> messages;
    FirebaseFirestore db;

    @Override
    public void onCreate() {
        super.onCreate();

        this.messages = new ArrayList<>();
        this.db = FirebaseFirestore.getInstance();

        CollectionReference cr = db.collection(FB_MEESAGES_KEY);
        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null){
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        Message msg = queryDocumentSnapshot.toObject(Message.class);
                        messages.add(msg);
                    }
                    Collections.sort(messages);
            } else {
                    Gson gson = new Gson();
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApp());
                    String messagesListGson = sp.getString(SP_ALL_MESSAGES, null);
                    if (messagesListGson != null) {
                        messages = gson.fromJson(messagesListGson,
                                new TypeToken<ArrayList<Message>>(){}.getType());
                    }
                }
                startChatActivity();
        }});
    }

    private void startChatActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public FirebaseFirestore getDataBase() {
        return db;
    }

    public SelfChatApp getApp() {
        return this;
    }
}
