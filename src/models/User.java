package models;

import commons.IdHelper;

/**
 * @author Janusz.
 */
public class User {
    private String name;
    private int id;
    private Account account;

    /**
     * Constructor
     * @param name
     */
    public User(String name) {
        this.name = name;
        this.id = IdHelper.getInstance().getNewId();
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

