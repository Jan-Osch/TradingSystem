package application.interactors;

import application.entities.User;
import commons.InteractorTransactionWithResult;
import persistance.EntityGateWayManager;
import commons.InteractorTransaction;
import persistance.UserGateWay;

public class RegisterUserTransaction implements InteractorTransactionWithResult {
    private final String password;
    private final String login;

    public RegisterUserTransaction(String password, String login) {
        this.password = password;
        this.login = login;
    }

    @Override
    public Boolean execute() {
        UserGateWay userGateWay = EntityGateWayManager.getUserGateway();
        User previousNameHolder = userGateWay.getUserByLogin(this.login);
        if (previousNameHolder == null) {
            User user = new User(this.login, this.password);
            userGateWay.saveUser(user);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
