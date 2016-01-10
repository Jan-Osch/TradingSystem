import database.*;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.exceptions.MarketNotFoundException;
import persistance.EntityGateWayManager;

import java.util.Iterator;

public class InitializeApplication {
    public static Market initializeMarket() {

        PostgresMarketDao postgresMarketDao = new PostgresMarketDao();
        PostgresStockDao postgresStockDao = new PostgresStockDao();
        PostgresHistoricalStockRecordDao postgresHistoricalStockRecordDao = new PostgresHistoricalStockRecordDao();
        PostgresIndexDao postgresIndexDao = new PostgresIndexDao();
        PostgresCurrentStockRecordDao postgresCurrentStockRecordDao = new PostgresCurrentStockRecordDao();

        EntityGateWayManager.setHistoricalStockRecordGateWay(postgresHistoricalStockRecordDao);
        EntityGateWayManager.setMarketGateWay(postgresMarketDao);
        EntityGateWayManager.setStockGateWay(postgresStockDao);
        EntityGateWayManager.setIndexGateWay(postgresIndexDao);
        EntityGateWayManager.setCurrentStockRecordGateWay(postgresCurrentStockRecordDao);


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
