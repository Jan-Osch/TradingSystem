package persistance;

import application.entities.User;
import markets.entities.market.Market;

import java.util.List;
import java.util.UUID;

public interface MarketGateWay {
    void saveMarket(Market market);

    Market getMarketByUuid(UUID uuid);

    List<Market> getAllMarkets();
}
