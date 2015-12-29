package application.entities;

import java.util.UUID;

public class User {
    private final String login;
    private String password;
    private final UUID uuid;


    public User(String login, String password, UUID uuid) {
        this.login = login;
        this.password = password;
        this.uuid = uuid;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
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
}
