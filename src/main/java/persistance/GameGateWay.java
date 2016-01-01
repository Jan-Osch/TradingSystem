package persistance;

import game.entities.Game;

import java.util.UUID;

public interface GameGateWay {

    void save(Game game);

    Iterable<Game> geAllGames();

    Game getGameByUuid(UUID gameUuid);
}
