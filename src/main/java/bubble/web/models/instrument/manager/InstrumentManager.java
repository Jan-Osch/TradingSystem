package bubble.web.models.instrument.manager;

import bubble.web.models.instrument.Instrument;
import bubble.web.models.instrument.InstrumentType;
import bubble.web.models.instrument.quotes.cache.InstrumentQuotesCache;
import bubble.web.models.instrument.quotes.interval.container.InstrumentQuotesIntervalContainer;
import bubble.web.models.record.Record;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public abstract class InstrumentManager {
    private Map<String, Instrument> trackedInstruments;
    private InstrumentQuotesCache instrumentQuotesCache;
    private InstrumentQuotesIntervalContainer instrumentQuotesIntervalContainer;
    private InstrumentType instrumentType;
    private long interval;
    private long offsetBeforeStartingTransfer;
    private Timer timer;

    public InstrumentManager(InstrumentQuotesCache instrumentQuotesCache, InstrumentQuotesIntervalContainer instrumentQuotesIntervalContainer, InstrumentType instrumentType, long offsetBeforeStartingTransfer, long interval) {
        this.instrumentQuotesCache = instrumentQuotesCache;
        this.trackedInstruments = new HashMap<String, Instrument>();
        this.instrumentQuotesIntervalContainer = instrumentQuotesIntervalContainer;
        this.instrumentType = instrumentType;
        this.interval = interval;
        this.offsetBeforeStartingTransfer = offsetBeforeStartingTransfer;
        this.startIntervalDataTransfer();
    }

    private void startIntervalDataTransfer() {
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new InstrumentManagerDataTransferTask(this), this.offsetBeforeStartingTransfer, this.interval);
    }

    public Boolean tracksInstrumentWithCode(String code) {
        return this.trackedInstruments.containsKey(code);
    }

    public Instrument getInstrumentByCode(String code) {
        if (this.tracksInstrumentWithCode(code)) {
            return this.trackedInstruments.get(code);
        }
        return null;
    }

    public abstract void createInstrumentByCode(String code);

    public Map<String, Instrument> getTrackedInstruments() {
        return trackedInstruments;
    }

    public void pushRecord(Record record) {
        this.instrumentQuotesCache.insertUpdateRecord(record);
    }

    public InstrumentType getInstrumentType() {
        return instrumentType;
    }

    public void transferData() {
        this.instrumentQuotesIntervalContainer.propagateRecords(this.instrumentQuotesCache.getRecords());
    }
}
