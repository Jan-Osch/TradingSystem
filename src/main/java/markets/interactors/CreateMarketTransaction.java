package markets.interactors;

import commons.InteractorTransactionWithException;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.exceptions.MarketByThisNameAlreadyExistsException;
import persistance.EntityGateWayManager;
import persistance.MarketGateWay;

import java.util.Objects;
import java.util.UUID;

public class CreateMarketTransaction implements InteractorTransactionWithException {
    private String marketName;

    public CreateMarketTransaction(String marketName) {
        this.marketName = marketName;
    }

    @Override
    public void execute() throws MarketByThisNameAlreadyExistsException {
        reloadMarkets();
        Iterable<Market> currentlyPresentMarkets = MarketManager.getAllMarkets();
        for (Market market : currentlyPresentMarkets) {
            if (Objects.equals(market.getName(), this.marketName)) {
                throw new MarketByThisNameAlreadyExistsException();
            }
        }
        createNewMarketAddToManagerAndPersist();
    }

    private void reloadMarkets() {
        LoadMarketsTransaction loadMarketsTransaction = new LoadMarketsTransaction();
        loadMarketsTransaction.execute();
    }

    private void createNewMarketAddToManagerAndPersist() {
        Market market = new Market(this.marketName, UUID.randomUUID());
        MarketGateWay marketGateWay = EntityGateWayManager.getMarketGateWay();
        marketGateWay.saveMarket(market);
        MarketManager.addMarket(market);
    }
}
