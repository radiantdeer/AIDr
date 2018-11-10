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
    private boolean isDisease;
    private boolean showLocations;
    private double lat = 0;
    private double lon = 0;
    final static private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");

    public Message(String text, String id, Author author) {
        this.text = text;
        this.id = id;
        this.author = author;
        this.tstamp = new Date();
        this.detailId = -1;
        this.isDisease = false;
        this.showLocations = false;
    }

    public Message(String text, String id, Author author, Date tstamp) {
        this.text = text;
        this.id = id;
        this.author = author;
        this.tstamp = tstamp;
        this.detailId = -1;
        this.isDisease = false;
        this.showLocations = false;
    }

    public Message(String text, String id, Author author, Date tstamp, int detailId) {
        this.text = text;
        this.id = id;
        this.author = author;
        this.tstamp = tstamp;
        this.detailId = detailId;
        this.isDisease = false;
        this.showLocations = false;
    }

    public Message(String text, String id, Author author, Date tstamp, int detailId, boolean isDisease) {
        this.text = text;
        this.id = id;
        this.author = author;
        this.tstamp = tstamp;
        this.detailId = detailId;
        this.isDisease = isDisease;
        this.showLocations = false;
    }

    public Message(String text, String id, Author author, Date tstamp, boolean showLocations) {
        this.text = text;
        this.id = id;
        this.author = author;
        this.tstamp = tstamp;
        this.detailId = -1;
        this.isDisease = false;
        this.showLocations = showLocations;
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

    public boolean isShowingLocations() { return showLocations; }

    public boolean isDisease() { return isDisease; }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public void setShowLocations(boolean showLocations) { this.showLocations = showLocations; }

    public void setIsDisease(boolean isDisease) { this.isDisease = isDisease; }

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer("{" +
                "\"id\" : \"" + id + "\"," +
                "\"text\" : \"" + text + "\"," +
                "\"author\" : " + author + "," +
                "\"tstamp\" : \"" + sdf.format(tstamp) + "\"," +
                "\"detailId\" : " + detailId + "," +
                "\"isDisease\" : " + isDisease + "," +
                "\"showLocation\" : " + showLocations);
        if (showLocations) {
            out.append("," +
            "\"lat\" : " + lat + "," +
            "\"lon\" : " + lon);
        }
        out.append("}");
        return out.toString();
    }
}
