package bubble.web.models.instrument.manager;


import bubble.web.models.instrument.InstrumentType;
import bubble.web.models.instrument.Stock;
import bubble.web.models.instrument.quotes.cache.InstrumentQuotesCache;
import bubble.web.models.instrument.quotes.interval.container.InstrumentQuotesIntervalContainer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StockManager extends InstrumentManager {
    private Map<String, String> fullNameToStockCodeMap;

    public StockManager(InstrumentQuotesCache instrumentQuotesCache, InstrumentQuotesIntervalContainer instrumentQuotesIntervalContainer, long offset, long interval) {
        super(instrumentQuotesCache, instrumentQuotesIntervalContainer, InstrumentType.STOCK, offset, interval);
        this.fullNameToStockCodeMap = new ConcurrentHashMap<String, String>();
    }

    @Override
    public void createInstrumentByCode(String code) {
        if (super.getTrackedInstruments().containsKey(code)) {
            throw new IllegalArgumentException("Stock already tracked!");
        }
        super.getTrackedInstruments().put(code, new Stock(code, null));
    }

    public void createStock(String stockCode, String stockName) {
        if (super.getTrackedInstruments().containsKey(stockCode)) {
            throw new IllegalArgumentException("Stock already tracked!");
        }
        super.getTrackedInstruments().put(stockCode, new Stock(stockCode, stockName));
        this.fullNameToStockCodeMap.put(stockName, stockCode);
    }

    public String getStockCodeByFullName(String stockName) {
        return this.fullNameToStockCodeMap.get(stockName);
    }


}