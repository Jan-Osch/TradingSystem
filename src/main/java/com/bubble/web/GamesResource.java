package com.bubble.web;

import com.bubble.accounts.exceptions.AccountUuidNotFound;
import com.bubble.accounts.exceptions.OwnerAlreadyHasAccount;
import com.bubble.application.exceptions.UserDoesNotExist;
import com.bubble.application.interactors.ApplicationInteractor;
import com.bubble.commons.JsonHelper;
import com.bubble.game.exceptions.GameUuidNotFound;
import com.bubble.game.exceptions.PlayerNotFound;
import com.bubble.game.exceptions.UserIsAlreadyPlayer;
import com.bubble.game.exceptions.UserIsAlreadySpectator;
import com.bubble.game.interactors.GameInteractor;
import com.bubble.markets.exceptions.InstrumentUuidNotFoundException;
import com.bubble.markets.exceptions.MarketNotFoundException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.get;
import static spark.Spark.post;

public class GamesResource {
    private static final String API_CONTEXT = "/api/games";
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(GamesResource.class);

    public GamesResource() {
        setupEndpoints();
    }

    private void setupEndpoints() {
        get(API_CONTEXT, "application/json", (request, response) -> {
            return GameInteractor.getAllGames();
        }, new JsonTransformer());

        get(API_CONTEXT + "/:gameUuid", "application/json", (request, response) -> {
            UUID uuid = UUID.fromString(request.params("gameUuid"));
            try {
                return GameInteractor.getGameByUuid(uuid);
            } catch (GameUuidNotFound gameUuidNotFound) {
                gameUuidNotFound.printStackTrace();
                return "Game Uuid not found!";
            }
        }, new JsonTransformer());

        get(API_CONTEXT + "/owner/:ownerUuid", "application/json", (request, response) -> {
            UUID uuid = UUID.fromString(request.params("ownerUuid"));
            return GameInteractor.getAllGamesForUser(uuid);
        }, new JsonTransformer());

        post(API_CONTEXT + "/create", (request, response) -> {
            try {
                Map<String, String> postParams = JsonHelper.jsonToMapStringString(request.body());
                String gameName = postParams.get("gameName");
                UUID marketUuid = UUID.fromString(postParams.get("marketUuid"));
                BigDecimal initialAmount = new BigDecimal(postParams.get("initialAmount"));
                GameInteractor.createGame(gameName, marketUuid, initialAmount);
                return "OK";
            }catch (Exception e){
                response.status(402);
                return e;
            }
        });

        post(API_CONTEXT + "/:gameUuid/join-as-player", (request, response) -> {
            Map<String, String> postParams = JsonHelper.jsonToMapStringString(request.body());
            UUID gameUuid = UUID.fromString(request.params("gameUuid"));
            try {
                UUID userUuid = UUID.fromString(postParams.get("userUuid"));
                GameInteractor.joinGameAsPlayer(gameUuid, userUuid);
            } catch (NullPointerException e) {
                e.printStackTrace();
                return "invalid params";
            } catch (UserIsAlreadySpectator e) {
                e.printStackTrace();
                return "Already a spectator";
            } catch (GameUuidNotFound e) {
                e.printStackTrace();
                return "Game does not exist";
            } catch (OwnerAlreadyHasAccount | UserIsAlreadyPlayer e) {
                e.printStackTrace();
                return "Already a player";
            }
            return "OK";
        }, new JsonTransformer());

        post(API_CONTEXT + "/:gameUuid/join-as-spectator", (request, response) -> {
            Map<String, String> postParams = JsonHelper.jsonToMapStringString(request.body());
            UUID gameUuid = UUID.fromString(request.params("gameUuid"));
            try {
                UUID userUuid = UUID.fromString(postParams.get("userUuid"));
                LOG.debug(String.format("join-as-spectator: gameUuid: %s  userUuid: %s", gameUuid.toString(), userUuid.toString()));
                GameInteractor.joinGameAsSpectator(gameUuid, userUuid);
            } catch (UserIsAlreadySpectator userIsAlreadySpectator) {
                userIsAlreadySpectator.printStackTrace();
                return "Already a spectator";
            } catch (UserIsAlreadyPlayer e) {
                e.printStackTrace();
                return "Already a player";
            } catch (GameUuidNotFound e) {
                e.printStackTrace();
                return "Game does not exist";
            }
            return "OK";
        }, new JsonTransformer());

        get(API_CONTEXT + "/:gameUuid/player/:ownerUuid", (request, response) -> {
            UUID gameUuid = UUID.fromString(request.params("gameUuid"));
            UUID ownerUuid = UUID.fromString(request.params("ownerUuid"));
            try {
                return GameInteractor.getPlayerUuid(gameUuid, ownerUuid);
            } catch (PlayerNotFound playerNotFound) {
                playerNotFound.printStackTrace();
                return "Player not found";
            }
        });

        get(API_CONTEXT + "/:gameUuid/ranking", (request, response) -> {
            UUID gameUuid = UUID.fromString(request.params("gameUuid"));
            try {
                Map<UUID, BigDecimal> ranking = GameInteractor.getRanking(gameUuid);
                Map<UUID, UUID> playerOwnerUuids = GameInteractor.getPlayerOwnerUuids(gameUuid);
                Map<UUID, String> playerToUserNameMap = new HashMap<>();
                for (UUID uuid : ranking.keySet()) {
                    playerToUserNameMap.put(uuid, ApplicationInteractor.getUserName(playerOwnerUuids.get(uuid)));
                }
                Map<String, Object> result = new HashMap<>();
                result.put("Ranking", ranking);
                result.put("UserNames", playerToUserNameMap);
                return result;
            } catch (GameUuidNotFound | InstrumentUuidNotFoundException | MarketNotFoundException | AccountUuidNotFound gameUuidNotFound) {
                gameUuidNotFound.printStackTrace();
                response.status(404);
                return "";
            } catch (UserDoesNotExist userDoesNotExist) {
                userDoesNotExist.printStackTrace();
                response.status(404);
                return "";
            }
        }, new JsonTransformer());
    }
}