import application.entities.User;
import database.PostgresUserDao;
import persistance.EntityGateWayManager;
import persistance.UserGateWay;

public class UserSavingTest {
    public static void main(String[] args) {
        User user = new User("jasiek", "12345dupa");
        PostgresUserDao postgresUserDao = new PostgresUserDao();
        EntityGateWayManager.setUserGateway(postgresUserDao);
        UserGateWay userGateWay = EntityGateWayManager.getUserGateway();
        userGateWay.saveUser(user);
    }
}
