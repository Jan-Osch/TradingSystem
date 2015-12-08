package markets.entities.manager;

import markets.entities.instrument.Index;
import markets.entities.instrument.InstrumentType;
import markets.entities.manager.quotes.cache.InstrumentQuotesCache;
import markets.entities.manager.quotes.container.InstrumentQuotesHistoricalContainer;
import markets.entities.manager.quotes.container.InstrumentQuotesIntervalContainer;

public class IndexManager extends InstrumentManager {

    public IndexManager(InstrumentQuotesCache instrumentQuotesCache, InstrumentQuotesIntervalContainer instrumentQuotesIntervalContainer, InstrumentQuotesHistoricalContainer instrumentQuotesHistoricalContainer, InstrumentType instrumentType, long offsetBeforeStartingTransfer, long interval) {
        super(instrumentQuotesCache, instrumentQuotesIntervalContainer, instrumentQuotesHistoricalContainer, instrumentType, offsetBeforeStartingTransfer, interval);
    }

    @Override
    public void createInstrumentByCode(String code) throws InstrumentManager.InstrumentAlreadyTracked {
        super.addInstrumentToManager(code, new Index(code, null));
    }
}
