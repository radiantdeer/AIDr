package com.aidr.aidr.Model;

import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.Date;

public class Message implements IMessage {

    private String text;
    private String id;
    private Author author;
    private Date tstamp;

    public Message(String text, String id, Author author) {
        this.text = text;
        this.id = id;
        this.author = author;
        this.tstamp = new Date();
    }

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
