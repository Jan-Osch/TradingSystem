package com.bubble.persistance;


public class EntityGateWayManager {
    private static UserGateWay userGateway;
    private static MarketGateWay marketGateWay;
    private static StockGateWay stockGateWay;
    private static IndexGateWay indexGateWay;
    private static HistoricalStockRecordGateWay historicalStockRecordGateWay;
    private static CurrentStockRecordGateWay currentStockRecordGateWay;
    private static AccountGateWay accountGateWay;
    private static GameGateWay gameGateWay;
    private static TransactionGateWay transactionGateWay;

    public static void setStockGateWay(StockGateWay stockGateWay) {
        EntityGateWayManager.stockGateWay = stockGateWay;
    }

    public static StockGateWay getStockGateWay() {
        return EntityGateWayManager.stockGateWay;
    }

    public static MarketGateWay getMarketGateWay() {
        return EntityGateWayManager.marketGateWay;
    }

    public static void setMarketGateWay(MarketGateWay marketGateWay) {
        EntityGateWayManager.marketGateWay = marketGateWay;
    }

    public static UserGateWay getUserGateway() {
        return EntityGateWayManager.userGateway;
    }

    public static void setUserGateway(UserGateWay userGateway) {
        EntityGateWayManager.userGateway = userGateway;
    }

    public static IndexGateWay getIndexGateWay() {
        return EntityGateWayManager.indexGateWay;
    }

    public static HistoricalStockRecordGateWay getHistoricalStockRecordGateWay() {
        return historicalStockRecordGateWay;
    }

    public static void setHistoricalStockRecordGateWay(HistoricalStockRecordGateWay historicalStockRecordGateWay) {
        EntityGateWayManager.historicalStockRecordGateWay = historicalStockRecordGateWay;
    }

    public static void setIndexGateWay(IndexGateWay indexGateWay) {
        EntityGateWayManager.indexGateWay = indexGateWay;
    }

    public static CurrentStockRecordGateWay getCurrentStockRecordGateWay() {
        return EntityGateWayManager.currentStockRecordGateWay;
    }

    public static void setCurrentStockRecordGateWay(CurrentStockRecordGateWay currentStockRecordGateWay) {
        EntityGateWayManager.currentStockRecordGateWay = currentStockRecordGateWay;
    }

    public static AccountGateWay getAccountGateWay() {
        return accountGateWay;
    }

    public static void setAccountGateWay(AccountGateWay accountGateWay) {
        EntityGateWayManager.accountGateWay = accountGateWay;
    }

    public static GameGateWay getGameGateWay() {
        return EntityGateWayManager.gameGateWay;
    }

    public static void setGameGateWay(GameGateWay gameGateWay) {
        EntityGateWayManager.gameGateWay = gameGateWay;
    }

    public static TransactionGateWay getTransactionGateWay() {
        return transactionGateWay;
    }

    public static void setTransactionGateWay(TransactionGateWay transactionGateWay) {
        EntityGateWayManager.transactionGateWay = transactionGateWay;
    }
}
