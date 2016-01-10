package com.bubble.application.interactors;

import com.bubble.application.entities.User;
import com.bubble.application.exceptions.InvalidPassword;
import com.bubble.application.exceptions.LoginAlreadyExists;
import com.bubble.application.exceptions.UserDoesNotExist;
import com.bubble.persistance.EntityGateWayManager;
import com.bubble.persistance.UserGateWay;

import java.util.UUID;

public class ApplicationInteractor {
    private static UserGateWay userGateWay = EntityGateWayManager.getUserGateway();

    public static UUID registerUser(String login, String userPassword) throws LoginAlreadyExists {
        User previousNameHolder = userGateWay.getUserByLogin(login);
        if (previousNameHolder == null) {
            User user = new User(login, userPassword);
            userGateWay.saveUser(user);
            return user.getUuid();
        }
        throw new LoginAlreadyExists();
    }

    public static UUID loginUser(String login, String password) throws UserDoesNotExist, InvalidPassword {
        User user = userGateWay.getUserByLogin(login);
        validateUserExists(user);
        if (!user.getPassword().equals(password)) {
            throw new InvalidPassword();
        }
        return user.getUuid();
    }

    public static void changePassword(UUID uuid, String password) throws UserDoesNotExist {
        User user = userGateWay.getUserByUuid(uuid);
        validateUserExists(user);
        user.setPassword(password);
        userGateWay.updateUser(user);
    }

    public static void unregisterUser(UUID uuid) throws UserDoesNotExist {
        User user = userGateWay.getUserByUuid(uuid);
        validateUserExists(user);
        userGateWay.deleteUser(user);
    }

    private static void validateUserExists(User user) throws UserDoesNotExist {
        if (user == null) {
            throw new UserDoesNotExist();
        }
    }
}
