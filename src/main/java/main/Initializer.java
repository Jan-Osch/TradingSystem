package main;

import database.*;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.exceptions.MarketNotFoundException;
import markets.interactors.LoadInstrumentsForMarketTransaction;
import markets.interactors.LoadMarketsTransaction;
import persistance.EntityGateWayManager;


public class Initializer {

    public static void start() {

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
        for (Market market : markets) {

            LoadInstrumentsForMarketTransaction loadInstrumentsForMarketTransaction = new LoadInstrumentsForMarketTransaction(market.getUuid());
            try {
                loadInstrumentsForMarketTransaction.execute();
            } catch (MarketNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

