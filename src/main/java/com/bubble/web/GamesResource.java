package web;

import accounts.exceptions.OwnerAlreadyHasAccount;
import commons.JsonHelper;
import game.exceptions.GameUuidNotFound;
import game.exceptions.PlayerNotFound;
import game.exceptions.UserIsAlreadyPlayer;
import game.exceptions.UserIsAlreadySpectator;
import game.interactors.GameInteractor;

import java.util.Map;
import java.util.UUID;

import static spark.Spark.get;
import static spark.Spark.post;

public class GamesResource {
    private static final String API_CONTEXT = "/api/games";

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
        }, new JsonTransformer());
    }
}