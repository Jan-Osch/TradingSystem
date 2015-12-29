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
        List<Market> markets= marketGateWay.getAllMarkets();
        MarketManager marketManager = MarketManager.getInstance();
        for(Market market :markets){
            marketManager.addMarket(market);
        }
    }
}
