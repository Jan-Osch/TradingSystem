import application.entities.User;
import database.PostgresUserDao;
import persistance.EntityGateWayManager;
import persistance.UserGateWay;

import java.util.UUID;

public class UserUpdateTest {
    public static void main(String[] args) {
        PostgresUserDao postgresUserDao = new PostgresUserDao();
        EntityGateWayManager.setUserGateway(postgresUserDao);
        UserGateWay userGateWay = EntityGateWayManager.getUserGateway();
        User user = userGateWay.getUserByUuid(UUID.fromString("b10ecf1f-7148-4089-8ca6-3f804acfd516"));
        user.setPassword("noweHaslo");
        userGateWay.updateUser(user);
    }
}
