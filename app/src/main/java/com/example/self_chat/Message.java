package com.example.self_chat;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {

    private final String text;

    Message(String message) {

        this.text = message;
    }

    private Message(Parcel in) {
        text = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    String getText()
    {
        return this.text;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
    }
}
