package com.bubble.application.entities;

import java.util.UUID;

public class User {
    private final String login;
    private String password;
    private final UUID uuid;
    private final boolean moderator;

    public User(String login, String password, UUID uuid, boolean moderator) {
        this.login = login;
        this.password = password;
        this.uuid = uuid;
        this.moderator = moderator;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.moderator = false;
        this.uuid = UUID.randomUUID();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getLogin() {
        return login;
    }

    public boolean getModerator() {
        return moderator;
    }
}
