package markets.entities.market;

import java.util.HashMap;
import java.util.UUID;

public class MarketManager {
    static MarketManager instance = null;
    public static HashMap<UUID, Market> marketHashMap;

    private MarketManager() {
        marketHashMap = new HashMap<>();
    }

    public static MarketManager getInstance() {
        if (instance == null) {
            instance = new MarketManager();
        }
        return instance;
    }

    public static void addMarket(Market market) {
        marketHashMap.put(market.getUuid(), market);
    }
}
