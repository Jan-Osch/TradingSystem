package com.bubble.persistance;


import com.bubble.application.entities.User;

import java.util.UUID;

public interface UserGateWay {
    void saveUser(User user);

    User getUserByUuid(UUID uuid);

    void updateUser(User user);

    void deleteUser(User user);

    User getUserByLogin(String login);
}
