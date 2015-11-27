package bubble.web.instruments.instrument.manager;


import bubble.web.instruments.instrument.InstrumentType;
import bubble.web.instruments.instrument.Stock;
import bubble.web.instruments.instrument.quotes.cache.InstrumentQuotesCache;
import bubble.web.instruments.instrument.quotes.container.InstrumentQuotesHistoricalContainer;
import bubble.web.instruments.instrument.quotes.container.InstrumentQuotesIntervalContainer;

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