package application.interactors;

import application.entities.User;
import commons.InteractorTransaction;
import persistance.EntityGateWayManager;
import persistance.UserGateWay;

import java.util.UUID;

public class UnregisterUserTransaction implements InteractorTransaction {
    private final UUID uuid;

    public UnregisterUserTransaction(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void execute() {
        UserGateWay userGateWay = EntityGateWayManager.getUserGateway();
        User user = userGateWay.getUserByUuid(uuid);
        userGateWay.deleteUser(user);
    }
}
