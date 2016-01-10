package com.bubble.scraper;

import com.bubble.markets.entities.instrument.Instrument;
import com.bubble.markets.entities.instrument.Stock;
import com.bubble.markets.entities.market.Market;
import com.bubble.markets.entities.market.MarketManager;
import com.bubble.markets.exceptions.InstrumentCodeAlreadyOnMarketException;
import com.bubble.markets.exceptions.MarketNotFoundException;
import com.bubble.markets.interactors.MarketsInteractor;
import com.google.common.collect.Iterables;

import java.util.UUID;

public abstract class StockScraper {

    protected Market market;
    protected UUID marketUuid;

    public StockScraper(UUID marketUuid) {
        this.marketUuid = marketUuid;
        try {
            this.market = MarketManager.getMarketByUuid(marketUuid);
        } catch (MarketNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected Stock createNewStockOnMarket(String stockName, String stockCode) {
        Stock stock = new Stock(UUID.randomUUID(), this.marketUuid, stockCode, stockName);

        try {
            MarketsInteractor.addInstrument(stock);
        } catch (MarketNotFoundException | InstrumentCodeAlreadyOnMarketException e) {
            e.printStackTrace();
        }
        return stock;
    }

    protected Stock getStockByCode(String stockCode, Iterable<Instrument> allInstruments) {
        return (Stock) Iterables.find(allInstruments,
                t -> t.getCodeName().equals(stockCode));
    }

    protected Stock getStockByFullName(String stockName, Iterable<Instrument> allInstruments) {
        return (Stock) Iterables.find(allInstruments,
                t -> t.getFullName().equals(stockName));
    }
}
