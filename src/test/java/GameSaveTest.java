import database.PostgresGameDao;
import game.entities.Game;

import java.math.BigDecimal;
import java.util.UUID;

public class GameSaveTest {

    public static void main(String[] args) {
        PostgresGameDao postgresGameDao = new PostgresGameDao();
//        Game game = new Game("Nowa wspaniala gra", UUID.randomUUID(), BigDecimal.valueOf(421798));

        Iterable<Game> games = postgresGameDao.geAllGames();
        games.iterator();


    }
}
