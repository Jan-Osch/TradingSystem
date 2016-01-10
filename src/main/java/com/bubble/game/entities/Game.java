package com.bubble.game.entities;

import com.bubble.game.exceptions.PlayerNotFound;
import com.bubble.game.exceptions.UserIsAlreadyPlayer;
import com.bubble.game.exceptions.UserIsAlreadySpectator;

import java.math.BigDecimal;
import java.util.*;

public class Game {
    public final String name;
    public final UUID gameUuid;
    public final UUID marketUuid;
    private Map<UUID, UUID> players;
    private List<UUID> spectators;
    private boolean active;
    private BigDecimal initialValue;

    public Game(String name, UUID gameUuid, UUID marketUuid, Map<UUID, UUID> players, List<UUID> spectators, BigDecimal initialValue, Boolean active) {
        this.name = name;
        this.gameUuid = gameUuid;
        this.marketUuid = marketUuid;
        this.players = players;
        this.spectators = spectators;
        this.initialValue = initialValue;
        this.active = active;
    }

    public Game(String name, UUID marketUuid, BigDecimal initialValue) {
        this(name, UUID.randomUUID(), marketUuid, new HashMap<>(), new LinkedList<>(), initialValue, true);
    }

    public UUID joinGameAsPlayer(UUID userUuid) throws UserIsAlreadyPlayer, UserIsAlreadySpectator {
        validateUserCanJoinGame(userUuid);
        return this.players.put(userUuid, UUID.randomUUID());
    }

    public void startGame() {
        this.active = true;
    }

    public void finishGame() {
        this.active = false;
    }

    public void joinGameAsSpectator(UUID userUuid) throws UserIsAlreadyPlayer, UserIsAlreadySpectator {
        validateUserCanJoinGame(userUuid);
        this.spectators.add(userUuid);
    }

    private void validateUserCanJoinGame(UUID userUuid) throws UserIsAlreadyPlayer, UserIsAlreadySpectator {
        if (isUserAPlayer(userUuid)) {
            throw new UserIsAlreadyPlayer();
        }
        if (isUserASpectator(userUuid)) {
            throw new UserIsAlreadySpectator();
        }
    }

    public UUID getPlayerUuidForUser(UUID userUUid) throws PlayerNotFound {
        UUID uuid = this.players.get(userUUid);
        if (uuid == null) {
            throw new PlayerNotFound();
        }
        return uuid;
    }

    public boolean isUserASpectator(UUID userUuid) {
        return this.spectators.contains(userUuid);
    }

    public boolean isUserAPlayer(UUID userUuid) {
        return this.players.keySet().contains(userUuid);
    }

    public boolean isUserInGame(UUID userUuid) {
        return isUserAPlayer(userUuid) || isUserASpectator(userUuid);
    }

    public BigDecimal getInitialValue() {
        return initialValue;
    }

    public boolean isActive() {
        return active;
    }

    public Map<UUID, UUID> getPlayers() {
        return players;
    }

    public List<UUID> getSpectators() {
        return spectators;
    }
}
