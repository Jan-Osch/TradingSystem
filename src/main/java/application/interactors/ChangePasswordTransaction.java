package application.interactors;

import application.entities.User;
import commons.InteractorTransaction;
import persistance.EntityGateWayManager;
import persistance.UserGateWay;

import java.util.UUID;

public class ChangePasswordTransaction implements InteractorTransaction {
    private UUID uuid;
    private String password;

    public ChangePasswordTransaction(UUID uuid, String password) {
        this.uuid = uuid;
        this.password = password;
    }

    @Override
    public void execute() {
        UserGateWay userGateWay = EntityGateWayManager.getUserGateway();
        User user = userGateWay.getUserByUuid(uuid);
        if (user != null) {
            user.setPassword(password);
            userGateWay.updateUser(user);
        }
    }
}
