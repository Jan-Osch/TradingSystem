package bubble.web.instruments.instrument.manager;

import bubble.web.instruments.instrument.Index;
import bubble.web.instruments.instrument.InstrumentType;
import bubble.web.instruments.instrument.quotes.cache.InstrumentQuotesCache;
import bubble.web.instruments.instrument.quotes.container.InstrumentQuotesHistoricalContainer;
import bubble.web.instruments.instrument.quotes.container.InstrumentQuotesIntervalContainer;

public class IndexManager extends InstrumentManager {

    public IndexManager(InstrumentQuotesCache instrumentQuotesCache, InstrumentQuotesIntervalContainer instrumentQuotesIntervalContainer, InstrumentQuotesHistoricalContainer instrumentQuotesHistoricalContainer, InstrumentType instrumentType, long offsetBeforeStartingTransfer, long interval) {
        super(instrumentQuotesCache, instrumentQuotesIntervalContainer, instrumentQuotesHistoricalContainer, instrumentType, offsetBeforeStartingTransfer, interval);
    }

    @Override
    public void createInstrumentByCode(String code) throws InstrumentManager.InstrumentAlreadyTracked {
        super.addInstrumentToManager(code, new Index(code, null));
    }
}
