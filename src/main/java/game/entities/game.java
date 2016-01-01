//package account;
//
//import account.user.Player;
//import account.user.Spectator;
//import application.entities.User;
//import markets.entities.market.Market;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class game {
//    private String name;
//    private Market market;
//    private Map<User, Player> userPlayerMap;
//    private Map<User, Spectator> userSpectatorMap;
//
//    public game(Market market, String name) {
//        this.market = market;
//        this.name = name;
//        this.userPlayerMap = new HashMap<>();
//        this.userSpectatorMap = new HashMap<>();
//    }
//
//    public void joinGameAsPlayer(User user) throws UserIsAlreadyPlayer, UserIsAlreadySpectator {
//        validateUserCanJoinGame(user);
//        this.userPlayerMap.put(user, new Player());
//    }
//
//    public void joinGameAsSpectator(User user) throws UserIsAlreadyPlayer, UserIsAlreadySpectator {
//        validateUserCanJoinGame(user);
//        this.userSpectatorMap.put(user, new Spectator());
//    }
//
//    private void validateUserCanJoinGame(User user) throws UserIsAlreadyPlayer, UserIsAlreadySpectator {
//        if (isUserAPlayer(user)) {
//            throw new UserIsAlreadyPlayer();
//        }
//        if (isUserASpectator(user)) {
//            throw new UserIsAlreadySpectator();
//        }
//    }
//
//    private boolean isUserASpectator(User user) {
//        return this.userSpectatorMap.containsKey(user);
//    }
//
//    private boolean isUserAPlayer(User user) {
//        return this.userPlayerMap.containsKey(user);
//    }
//
//    protected class UserIsAlreadyPlayer extends Throwable {
//    }
//
//    private class UserIsAlreadySpectator extends Throwable {
//    }
//}
