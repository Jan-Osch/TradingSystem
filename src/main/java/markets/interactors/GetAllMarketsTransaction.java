package markets.interactors;

import commons.InteractorTransactionWithResult;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.exceptions.MarketNotFoundException;
import persistance.EntityGateWayManager;
import persistance.MarketGateWay;

public class GetAllMarketsTransaction implements InteractorTransactionWithResult {

    @Override
    public Iterable<Market> execute() throws MarketNotFoundException {
        MarketGateWay marketGateWay = EntityGateWayManager.getMarketGateWay();
        Iterable<Market> allMarkets = marketGateWay.getAllMarkets();
        for (Market market :allMarkets){
            MarketManager.addMarket(market);
        }
        return MarketManager.getAllMarkets();
    }
}
