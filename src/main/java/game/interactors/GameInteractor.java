package game.interactors;

import accounts.exceptions.OwnerAlreadyHasAccount;
import accounts.interactors.AccountsInteractor;
import com.google.common.collect.Iterables;
import game.entities.Game;
import game.exceptions.GameUuidNotFound;
import game.exceptions.PlayerNotFound;
import game.exceptions.UserIsAlreadyPlayer;
import game.exceptions.UserIsAlreadySpectator;
import persistance.EntityGateWayManager;
import persistance.GameGateWay;

import java.math.BigDecimal;
import java.util.UUID;

public class GameInteractor {
    static GameGateWay gameGateWay = EntityGateWayManager.getGameGateWay();

    public static void createGame(String name, UUID marketUuid, BigDecimal initialValue) {
        Game game = new Game(name, marketUuid, initialValue);
        gameGateWay.save(game);
    }

    public static Iterable<Game> getAllGames() {
        return gameGateWay.geAllGames();
    }

    public static Game getGameByUuid(UUID gameUuid) throws GameUuidNotFound {
        Game game = gameGateWay.getGameByUuid(gameUuid);
        if(game==null){
            throw new GameUuidNotFound();
        }
        return game;
    }

    public static void joinGameAsSpectator(UUID gameUuid, UUID userUuid) throws GameUuidNotFound, UserIsAlreadySpectator, UserIsAlreadyPlayer {
        Game game = gameGateWay.getGameByUuid(gameUuid);
        if (game == null) {
            throw new GameUuidNotFound();
        }
        game.joinGameAsSpectator(userUuid);
        gameGateWay.save(game);
    }

    public static void joinGameAsPlayer(UUID gameUuid, UUID userUuid) throws GameUuidNotFound, UserIsAlreadySpectator, UserIsAlreadyPlayer, OwnerAlreadyHasAccount {
        Game game = gameGateWay.getGameByUuid(gameUuid);
        if (game == null) {
            throw new GameUuidNotFound();
        }
        UUID playerUuid = game.joinGameAsPlayer(userUuid);
        gameGateWay.save(game);
        AccountsInteractor.createAccount(playerUuid, game.getInitialValue());
    }

    public static Iterable<Game> getAllGamesForUser(UUID userUuid) {
        Iterable<Game> allGames = getAllGames();
        return Iterables.filter(allGames, game -> game.isUserInGame(userUuid));
    }

    public static UUID getPlayerUuid(UUID gameUuid, UUID userUuid) throws PlayerNotFound {
        Game game = gameGateWay.getGameByUuid(gameUuid);
        return game.getPlayerUuidForUser(userUuid);
    }
}
