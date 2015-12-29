package application.interactors;

import application.entities.User;
import commons.InteractorTransaction;
import commons.InteractorTransactionWithResult;
import persistance.EntityGateWayManager;
import persistance.UserGateWay;

public class LoginTransaction implements InteractorTransactionWithResult {
    private final String password;
    private final String login;

    public LoginTransaction(String password, String login) {
        this.password = password;
        this.login = login;
    }

    @Override
    public User execute() {
        UserGateWay userGateWay = EntityGateWayManager.getUserGateway();
        User user = userGateWay.getUserByLogin(this.login);
        if (user != null && user.getPassword().equals(this.password)) {
            return user;
        } else {
            return null;
        }
    }
}
