package bubble.web.gameplay;

import bubble.web.application.user.User;
import bubble.web.gameplay.user.Player;
import bubble.web.gameplay.user.Spectator;
import bubble.web.instruments.market.Market;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private String name;
    private Market market;
    private Map<User, Player> userPlayerMap;
    private Map<User, Spectator> userSpectatorMap;

    public Game(Market market, String name) {
        this.market = market;
        this.name = name;
        this.userPlayerMap = new HashMap<>();
        this.userSpectatorMap = new HashMap<>();
    }

    public void joinGameAsPlayer(User user) throws UserIsAlreadyPlayer, UserIsAlreadySpectator {
        validateUserCanJoinGame(user);
        this.userPlayerMap.put(user, new Player());
    }

    public void joinGameAsSpectator(User user) throws UserIsAlreadyPlayer, UserIsAlreadySpectator {
        validateUserCanJoinGame(user);
        this.userSpectatorMap.put(user, new Spectator());
    }

    private void validateUserCanJoinGame(User user) throws UserIsAlreadyPlayer, UserIsAlreadySpectator {
        if (isUserAPlayer(user)) {
            throw new UserIsAlreadyPlayer();
        }
        if (isUserASpectator(user)) {
            throw new UserIsAlreadySpectator();
        }
    }

    private boolean isUserASpectator(User user) {
        return this.userSpectatorMap.containsKey(user);
    }

    private boolean isUserAPlayer(User user) {
        return this.userPlayerMap.containsKey(user);
    }

    protected class UserIsAlreadyPlayer extends Throwable {
    }

    private class UserIsAlreadySpectator extends Throwable {
    }
}
