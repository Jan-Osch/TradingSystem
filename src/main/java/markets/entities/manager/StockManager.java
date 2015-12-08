package markets.entities.manager;


import markets.entities.manager.quotes.container.InstrumentQuotesHistoricalContainer;
import markets.entities.instrument.InstrumentType;
import markets.entities.instrument.Stock;
import markets.entities.manager.quotes.cache.InstrumentQuotesCache;
import markets.entities.manager.quotes.container.InstrumentQuotesIntervalContainer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StockManager extends InstrumentManager {
    private Map<String, String> fullNameToStockCodeMap;

    public StockManager(InstrumentQuotesCache instrumentQuotesCache, InstrumentQuotesIntervalContainer instrumentQuotesIntervalContainer, InstrumentQuotesHistoricalContainer instrumentQuotesHistoricalContainer, long offset, long interval) {
        super(instrumentQuotesCache, instrumentQuotesIntervalContainer, instrumentQuotesHistoricalContainer, InstrumentType.STOCK, offset, interval);
        this.fullNameToStockCodeMap = new ConcurrentHashMap<String, String>();
    }

    public void createStock(String stockCode, String stockName) throws InstrumentAlreadyTracked {
        super.addInstrumentToManager(stockCode, new Stock(stockCode, stockName));
        this.fullNameToStockCodeMap.put(stockName, stockCode);
    }

    public String getStockCodeByFullName(String stockName) {
        return this.fullNameToStockCodeMap.get(stockName);
    }

    @Override
    public void createInstrumentByCode(String stockCode) throws InstrumentAlreadyTracked {
        super.addInstrumentToManager(stockCode, new Stock(stockCode, null));
    }
}