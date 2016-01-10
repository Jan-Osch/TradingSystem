package com.bubble.persistance;


import com.bubble.markets.entities.market.Market;

import java.util.UUID;

public interface MarketGateWay {
    void saveMarket(Market market);

    Market getMarketByUuid(UUID uuid);

    Iterable<Market> getAllMarkets();
}
