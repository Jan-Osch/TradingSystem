package markets.entities.manager;

import markets.entities.manager.quotes.container.InstrumentQuotesHistoricalContainer;
import markets.entities.instrument.Instrument;
import markets.entities.instrument.InstrumentType;
import markets.entities.manager.quotes.cache.InstrumentQuotesCache;
import markets.entities.manager.quotes.container.InstrumentQuotesIntervalContainer;
import markets.entities.record.Record;

import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InstrumentManager {
    private Map<String, Instrument> trackedInstruments;
    private InstrumentQuotesCache instrumentQuotesCache;
    private InstrumentQuotesIntervalContainer instrumentQuotesIntervalContainer;
    private InstrumentQuotesHistoricalContainer instrumentQuotesHistoricalContainer;
    private InstrumentType instrumentType;
    private long interval;
    private long offsetBeforeStartingTransfer;
    private Timer timer;

    public InstrumentManager(InstrumentQuotesCache instrumentQuotesCache, InstrumentQuotesIntervalContainer instrumentQuotesIntervalContainer, InstrumentQuotesHistoricalContainer instrumentQuotesHistoricalContainer, InstrumentType instrumentType, long offsetBeforeStartingTransfer, long interval) {
        this.instrumentQuotesCache = instrumentQuotesCache;
        this.instrumentQuotesHistoricalContainer = instrumentQuotesHistoricalContainer;
        this.instrumentQuotesIntervalContainer = instrumentQuotesIntervalContainer;
        this.instrumentType = instrumentType;
        this.interval = interval;
        this.offsetBeforeStartingTransfer = offsetBeforeStartingTransfer;
        this.trackedInstruments = new ConcurrentHashMap<String, Instrument>();
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

    public void addInstrumentToManager(String code, Instrument instrument) throws InstrumentAlreadyTracked {
        if(this.trackedInstruments.containsKey(code)){
            throw new InstrumentAlreadyTracked();
        }
        this.trackedInstruments.put(code, instrument);
    }

    public abstract void createInstrumentByCode(String code) throws InstrumentAlreadyTracked;

    public Map<String, Instrument> getTrackedInstruments() {
        return trackedInstruments;
    }

    public void pushRecord(Record record) {
        this.instrumentQuotesCache.insertUpdateRecord(record);
    }

    public void pushHistoricalRecord(Record record) {
        this.instrumentQuotesHistoricalContainer.pushRecordToContainer(record);
    }

    public InstrumentType getInstrumentType() {
        return instrumentType;
    }

    public void transferData() {
        this.instrumentQuotesIntervalContainer.propagateRecords(this.instrumentQuotesCache.getRecords());
    }

    public static class InstrumentAlreadyTracked extends Throwable {
    }
}
