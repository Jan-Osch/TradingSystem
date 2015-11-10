package bubble.web.models.instrument.manager;


import bubble.web.models.instrument.InstrumentType;
import bubble.web.models.instrument.Stock;
import bubble.web.models.instrument.quotes.cache.InstrumentQuotesCache;
import bubble.web.models.instrument.quotes.interval.container.InstrumentQuotesIntervalContainer;

/**
 * @author Janusz.
 */
public class StockManager extends InstrumentManager{
    public StockManager(InstrumentQuotesCache instrumentQuotesCache, InstrumentQuotesIntervalContainer instrumentQuotesIntervalContainer) {
        super(instrumentQuotesCache, instrumentQuotesIntervalContainer, InstrumentType.STOCK);
    }

    @Override
    public void createInstrumentByCode(String code) {
        if(super.getTrackedInstruments().containsKey(code)){
            throw new IllegalArgumentException("Stock alredy tracked!");
        }
        super.getTrackedInstruments().put(code, new Stock(code, null));
    }
}