import database.PostgresHistoricalStockRecordDao;
import database.PostgresIndexDao;
import database.PostgresMarketDao;
import database.PostgresStockDao;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.exceptions.MarketNotFoundException;
import markets.interactors.LoadInstrumentsForMarketTransaction;
import markets.interactors.LoadMarketsTransaction;
import persistance.EntityGateWayManager;

import java.util.Iterator;

public class InitializeApplication {
    public static Market initializeMarket() {

        PostgresMarketDao postgresMarketDao = new PostgresMarketDao();
        PostgresStockDao postgresStockDao = new PostgresStockDao();
        PostgresHistoricalStockRecordDao postgresHistoricalStockRecordDao = new PostgresHistoricalStockRecordDao();
        PostgresIndexDao postgresIndexDao = new PostgresIndexDao();

        EntityGateWayManager.setHistoricalStockRecordGateWay(postgresHistoricalStockRecordDao);
        EntityGateWayManager.setMarketGateWay(postgresMarketDao);
        EntityGateWayManager.setStockGateWay(postgresStockDao);
        EntityGateWayManager.setIndexGateWay(postgresIndexDao);


        LoadMarketsTransaction loadMarketsTransaction = new LoadMarketsTransaction();
        loadMarketsTransaction.execute();

        Iterable<Market> markets = MarketManager.getAllMarkets();
        Iterator i = markets.iterator();
        Market warsaw = (Market) i.next();


        LoadInstrumentsForMarketTransaction loadInstrumentsForMarketTransaction = new LoadInstrumentsForMarketTransaction(warsaw.getUuid());

        try {
            loadInstrumentsForMarketTransaction.execute();
        } catch (MarketNotFoundException e) {
            e.printStackTrace();
        }
        return warsaw;
    }
}
