package persistance;

public class EntityGateWayManager {
    private static UserGateWay userGateway;
    private static MarketGateWay marketGateWay;
    private static StockGateWay stockGateWay;

    public static void setStockGateWay(StockGateWay stockGateWay) {
        EntityGateWayManager.stockGateWay = stockGateWay;
    }

    public static StockGateWay getStockGateWay() {

        return stockGateWay;
    }

    public static MarketGateWay getMarketGateWay() {
        return marketGateWay;
    }

    public static void setMarketGateWay(MarketGateWay marketGateWay) {

        EntityGateWayManager.marketGateWay = marketGateWay;
    }

    public static UserGateWay getUserGateway() {
        return userGateway;
    }

    public static void setUserGateway(UserGateWay userGateway) {
        EntityGateWayManager.userGateway = userGateway;
    }
}
