package application.interactors;

import application.entities.User;
import persistance.EntityGateWayManager;
import commons.InteractorTransaction;
import persistance.UserGateWay;

public class RegisterUserTransaction implements InteractorTransaction {
    private final String password;
    private final String login;

    public RegisterUserTransaction(String password, String login) {
        this.password = password;
        this.login = login;
    }

    @Override
    public void execute() {
        User user = new User(this.login, this.password);
        UserGateWay userGateWay = EntityGateWayManager.getUserGateway();
        userGateWay.save(user);
    }
}
