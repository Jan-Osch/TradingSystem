package com.bubble.main;


import com.bubble.database.*;
import com.bubble.markets.entities.market.Market;
import com.bubble.markets.exceptions.MarketNotFoundException;
import com.bubble.markets.interactors.MarketsInteractor;
import com.bubble.persistance.EntityGateWayManager;

public class Initializer {

    public static void start() {

        PostgresMarketDao postgresMarketDao = new PostgresMarketDao();
        PostgresStockDao postgresStockDao = new PostgresStockDao();
        PostgresHistoricalStockRecordDao postgresHistoricalStockRecordDao = new PostgresHistoricalStockRecordDao();
        PostgresIndexDao postgresIndexDao = new PostgresIndexDao();
        PostgresAccountDao postgresAccountDao = new PostgresAccountDao();
        PostgresCurrentStockRecordDao postgresCurrentStockRecordDao = new PostgresCurrentStockRecordDao();
        PostgresGameDao postgresGameDao = new PostgresGameDao();
        PostgresUserDao postgresUserDao = new PostgresUserDao();

        EntityGateWayManager.setHistoricalStockRecordGateWay(postgresHistoricalStockRecordDao);
        EntityGateWayManager.setMarketGateWay(postgresMarketDao);
        EntityGateWayManager.setStockGateWay(postgresStockDao);
        EntityGateWayManager.setIndexGateWay(postgresIndexDao);
        EntityGateWayManager.setCurrentStockRecordGateWay(postgresCurrentStockRecordDao);
        EntityGateWayManager.setAccountGateWay(postgresAccountDao);
        EntityGateWayManager.setGameGateWay(postgresGameDao);
        EntityGateWayManager.setUserGateway(postgresUserDao);

        Iterable<Market> markets = MarketsInteractor.getAllMarkets();
        for (Market market : markets) {
            try {
                MarketsInteractor.loadInstrumentsForMarket(market.getUuid());
            } catch (MarketNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

