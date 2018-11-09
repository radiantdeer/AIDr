package com.aidr.aidr.Model;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements IMessage, MessageContentType {

    private String text;
    private String id;
    private Author author;
    private Date tstamp;
    private int detailId;
    final static private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");

    public Message(String text, String id, Author author) {
        this.text = text;
        this.id = id;
        this.author = author;
        this.tstamp = new Date();
        this.detailId = -1;
    }

    public Message(String text, String id, Author author, Date tstamp) {
        this.text = text;
        this.id = id;
        this.author = author;
        this.tstamp = tstamp;
        this.detailId = -1;
    }

    public Message(String text, String id, Author author, Date tstamp, int detailId) {
        this.text = text;
        this.id = id;
        this.author = author;
        this.tstamp = tstamp;
        this.detailId = detailId;
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

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\" : \"" + id + "\"," +
                "\"text\" : \"" + text + "\"," +
                "\"author\" : " + author + "," +
                "\"tstamp\" : \"" + sdf.format(tstamp) + "\"," +
                "\"detailId\" : " + detailId +
                "}";
    }
}
