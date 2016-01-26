package com.bubble.game.interactors;

import com.bubble.accounts.exceptions.AccountUuidNotFound;
import com.bubble.accounts.exceptions.OwnerAlreadyHasAccount;
import com.bubble.accounts.interactors.AccountsInteractor;
import com.bubble.game.entities.Game;
import com.bubble.game.exceptions.GameUuidNotFound;
import com.bubble.game.exceptions.PlayerNotFound;
import com.bubble.game.exceptions.UserIsAlreadyPlayer;
import com.bubble.game.exceptions.UserIsAlreadySpectator;
import com.bubble.markets.exceptions.InstrumentUuidNotFoundException;
import com.bubble.markets.exceptions.MarketNotFoundException;
import com.bubble.persistance.EntityGateWayManager;
import com.bubble.persistance.GameGateWay;
import com.bubble.transactions.interactors.TransactionsInteractor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
        List<Game> games = (List<Game>) getAllGames();
        return games.stream()
                .filter(game -> game.isUserInGame(userUuid))
                .collect(Collectors.toList());
    }

    public static UUID getPlayerUuid(UUID gameUuid, UUID userUuid) throws PlayerNotFound {
        Game game = gameGateWay.getGameByUuid(gameUuid);
        return game.getPlayerUuidForUser(userUuid);
    }

    public static Map<UUID, BigDecimal> getRanking(UUID gameUuid) throws GameUuidNotFound, InstrumentUuidNotFoundException, MarketNotFoundException, AccountUuidNotFound {
        Game game = getGameByUuid(gameUuid);
        HashMap<UUID, BigDecimal> result = new HashMap<>();
        for (UUID playerUuid : game.getPlayers().values()) {
            result.put(playerUuid, TransactionsInteractor.getTotalValueOfAccount(playerUuid));
        }
        return result;
    }

    public static Map<UUID, UUID> getPlayerOwnerUuids(UUID gameUuid) throws GameUuidNotFound {
        Game game = getGameByUuid(gameUuid);
        Map<UUID, UUID> gamePlayers = game.getPlayers();
        return gamePlayers.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
