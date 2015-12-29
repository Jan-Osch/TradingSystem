package markets.interactors;

import commons.InteractorTransaction;
import commons.InteractorTransactionWithResult;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import persistance.EntityGateWayManager;
import persistance.MarketGateWay;

import java.util.UUID;

public class CreateMarketTransaction implements InteractorTransaction {
    private String marketName;

    public CreateMarketTransaction(String marketName) {
        this.marketName = marketName;
    }

    @Override
    public void execute() {
        MarketGateWay marketGateWay = EntityGateWayManager.getMarketGateWay();
        Market market = new Market(this.marketName, UUID.randomUUID());
        marketGateWay.saveMarket(market);
        MarketManager marketManager = MarketManager.getInstance();
        marketManager.addMarket(market);
    }
}
