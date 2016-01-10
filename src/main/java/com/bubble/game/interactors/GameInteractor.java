package com.bubble.game.interactors;

import com.bubble.accounts.exceptions.OwnerAlreadyHasAccount;
import com.bubble.accounts.interactors.AccountsInteractor;
import com.bubble.game.entities.Game;
import com.bubble.game.exceptions.GameUuidNotFound;
import com.bubble.game.exceptions.PlayerNotFound;
import com.bubble.game.exceptions.UserIsAlreadyPlayer;
import com.bubble.game.exceptions.UserIsAlreadySpectator;
import com.bubble.persistance.EntityGateWayManager;
import com.bubble.persistance.GameGateWay;
import com.google.common.collect.Iterables;

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
        if (game == null) {
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
        gameGateWay.update(game);
    }

    public static void joinGameAsPlayer(UUID gameUuid, UUID userUuid) throws GameUuidNotFound, UserIsAlreadySpectator, UserIsAlreadyPlayer, OwnerAlreadyHasAccount {
        Game game = gameGateWay.getGameByUuid(gameUuid);
        if (game == null) {
            throw new GameUuidNotFound();
        }
        UUID playerUuid = game.joinGameAsPlayer(userUuid);
        gameGateWay.update(game);
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
