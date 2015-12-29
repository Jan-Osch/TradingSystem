import database.PostgresMarketDao;
import markets.entities.market.MarketManager;
import markets.interactors.CreateMarketTransaction;
import markets.interactors.LoadMarketsTransaction;
import persistance.EntityGateWayManager;

public class LoadMarketsTest {
    public static void main(String[] args) {
        PostgresMarketDao postgresMarketDao = new PostgresMarketDao();
        EntityGateWayManager.setMarketGateWay(postgresMarketDao);

//        CreateMarketTransaction createWarsaw = new CreateMarketTransaction("Warsaw");
//        createWarsaw.execute();
//
//        CreateMarketTransaction createLondon = new CreateMarketTransaction("London");
//        createLondon.execute();

        LoadMarketsTransaction loadMarketsTransaction = new LoadMarketsTransaction();
        loadMarketsTransaction.execute();

        MarketManager marketManager = MarketManager.getInstance();
        System.out.printf("Number of tracked markets: %d", MarketManager.marketHashMap.size());
    }
}
