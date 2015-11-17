package bubble.web.models.instrument.manager;

import bubble.web.models.instrument.Index;
import bubble.web.models.instrument.InstrumentType;
import bubble.web.models.instrument.quotes.cache.InstrumentQuotesCache;
import bubble.web.models.instrument.quotes.interval.container.InstrumentQuotesIntervalContainer;

public class IndexManager extends InstrumentManager {

    public IndexManager(InstrumentQuotesCache instrumentQuotesCache, InstrumentQuotesIntervalContainer instrumentQuotesIntervalContainer, long offset, long interval) {
        super(instrumentQuotesCache, instrumentQuotesIntervalContainer, InstrumentType.INDEX, offset, interval);
    }

    @Override
    public void createInstrumentByCode(String code) {
        if (super.getTrackedInstruments().containsKey(code)) {
            throw new IllegalArgumentException("Index Already Tracked!");
        }
        super.getTrackedInstruments().put(code, new Index(code, null));
    }
}
