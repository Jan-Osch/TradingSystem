import application.entities.User;
import database.PostgresUserDao;
import persistance.EntityGateWayManager;
import persistance.UserGateWay;

public class ApplicationTest {
    public static void main(String[] args) {
        PostgresUserDao postgresUserDao = new PostgresUserDao();
        EntityGateWayManager.setUserGateway(postgresUserDao);
        UserGateWay userGateWay = EntityGateWayManager.getUserGateway();


        RegisterUserTransaction registerUserTransaction = new RegisterUserTransaction("slabeHaslo", "Michau");
        Boolean resultRegister = registerUserTransaction.execute();
        System.out.println("Registering: "+resultRegister);

        RegisterUserTransaction registerUserTransactionSecond = new RegisterUserTransaction("inneHaslo", "Michau");
        Boolean resultRegisterSecond  = registerUserTransactionSecond.execute();
        System.out.println("Registering again: "+resultRegisterSecond);

        LoginTransaction loginTransaction =  new LoginTransaction("slabeHaslo" ,"Michau");
        User user= loginTransaction.execute();
        System.out.printf("Login: %s\n", user != null);

        LoginTransaction loginTransactionWrongPassword =  new LoginTransaction("wrongPassword" ,"Michau");
        User userWrong = loginTransactionWrongPassword.execute();
        System.out.printf("Login with wrong password: %s\n", userWrong!= null);

        ChangePasswordTransaction changePasswordTransaction = new ChangePasswordTransaction(user.getUuid(), "newPassword");
        changePasswordTransaction.execute();

        LoginTransaction loginThird =  new LoginTransaction("newPassword" ,"Michau");
        User userChanged= loginThird.execute();
        System.out.printf("Login changed password: %s\n", userChanged!= null);

        UnregisterUserTransaction unregisterUserTransaction = new UnregisterUserTransaction(user.getUuid());
        unregisterUserTransaction.execute();

        LoginTransaction loginAfterUnregister =  new LoginTransaction("newPassword" ,"Michau");
        User userUnregistered= loginAfterUnregister.execute();
        System.out.printf("Login after unregistered: %s\n", userUnregistered!= null);

    }

}
