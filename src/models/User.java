package models;

import commons.IdHelper;

public class User {
    private final String name;
    private final int id;
    private Account account;

    public User(String name) {
        this.id = IdHelper.getInstance().getNewId();
        this.name = name;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

