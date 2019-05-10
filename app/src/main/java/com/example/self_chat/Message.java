package com.example.self_chat;

import java.util.Date;

public class Message implements Comparable<Message> {

    private final String content;
    private final Date timeStamp;
    private final int id;

    public Message(){
        content = null;
        id = 0;
        timeStamp = null;
    }

    Message(String message) {
        this.content = message;
        this.timeStamp = new Date(System.currentTimeMillis());
        this.id = createMessageId(this.content, this.timeStamp);
    }

    String getContent() {
        return this.content;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    public Date getTimeStamp() {
        return this.timeStamp;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public int compareTo(Message o) {
        return this.timeStamp.compareTo(o.getTimeStamp());
    }

    private static int createMessageId(String content, Date date) {
        return (content + date.toString()).hashCode();
    }
}
