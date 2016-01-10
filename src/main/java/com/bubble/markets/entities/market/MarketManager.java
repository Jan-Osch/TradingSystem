package com.bubble.markets.entities.market;


import com.bubble.markets.exceptions.MarketNotFoundException;

import java.util.HashMap;
import java.util.UUID;

public class MarketManager {
    private static HashMap<UUID, Market> marketHashMap = new HashMap<>();

    public static void addMarket(Market market) {
        marketHashMap.putIfAbsent(market.getUuid(), market);
    }

    public static Iterable<Market> getAllMarkets() {
        return marketHashMap.values();
    }



    public static Market getMarketByUuid(UUID uuid) throws MarketNotFoundException {
        Market result = marketHashMap.get(uuid);
        if (result != null) {
            return result;
        }
        throw new MarketNotFoundException();
    }
}
