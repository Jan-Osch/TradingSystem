package persistance;

public class EntityGateWayManager {
    private static UserGateWay userGateway;
    private static MarketGateWay marketGateWay;
    private static StockGateWay stockGateWay;
    private static IndexGateWay indexGateWay;
    private static HistoricalStockRecordGateWay historicalStockRecordGateWay;

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
}
