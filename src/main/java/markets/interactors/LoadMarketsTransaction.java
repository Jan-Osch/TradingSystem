package markets.interactors;

import commons.InteractorTransaction;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import persistance.EntityGateWayManager;
import persistance.MarketGateWay;

import java.util.List;

public class LoadMarketsTransaction implements InteractorTransaction {

    @Override
    public void execute() {
        MarketGateWay marketGateWay= EntityGateWayManager.getMarketGateWay();
        Iterable<Market> markets= marketGateWay.getAllMarkets();
        markets.forEach(MarketManager::addMarket);
    }
}
