package com.bubble.persistance;


import com.bubble.markets.entities.instrument.Stock;

import java.util.UUID;

public interface StockGateWay {
    void saveStock(Stock stock);

    Stock getStockByUuid(UUID uuid);

    Iterable<Stock> getStocksByMarketUuid(UUID marketUuid);

}
