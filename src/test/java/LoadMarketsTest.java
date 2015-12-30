import com.google.common.collect.Iterables;
import database.PostgresMarketDao;
import markets.entities.market.MarketManager;
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

        System.out.printf("Number of tracked markets: %d", Iterables.size(MarketManager.getAllMarkets()));
    }
}
