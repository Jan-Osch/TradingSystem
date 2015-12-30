package scraper;

import com.google.common.collect.Iterables;
import markets.entities.instrument.Instrument;
import markets.entities.instrument.Stock;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.exceptions.InstrumentCodeAlreadyOnMarketException;
import markets.exceptions.MarketNotFoundException;
import markets.interactors.AddInstrumentTransaction;

import java.util.UUID;

public class StockScraper {

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

        AddInstrumentTransaction addInstrumentTransaction = new AddInstrumentTransaction(stock);
        try {
            addInstrumentTransaction.execute();
        } catch (MarketNotFoundException | InstrumentCodeAlreadyOnMarketException ignored) {
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
