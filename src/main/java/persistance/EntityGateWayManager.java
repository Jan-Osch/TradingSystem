package persistance;

import java.util.Map;

public class EntityGateWayManager {
    private static UserGateWay userGateway;

    public static UserGateWay getUserGateway() {
        return userGateway;
    }

    public static void setUserGateway(UserGateWay userGateway) {
        EntityGateWayManager.userGateway = userGateway;
    }
}
