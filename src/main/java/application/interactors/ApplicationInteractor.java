package application.interactors;

import application.entities.User;
import application.exceptions.InvalidPassword;
import application.exceptions.LoginAlreadyExists;
import application.exceptions.UserDoesNotExist;
import persistance.EntityGateWayManager;
import persistance.UserGateWay;

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
        if (user == null) {
            throw new UserDoesNotExist();
        }
        if (!user.getPassword().equals(password)) {
            throw new InvalidPassword();
        }
        return user.getUuid();
    }
}
