package com.aidr.aidr.Model;

import com.stfalcon.chatkit.commons.models.IUser;

public class Author implements IUser {

    private String id;
    private String name;
    private String avatar;

    public Author(String id, String name, String ava) {
        this.id = id;
        this.name = name;
        this.avatar = ava;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }
}
