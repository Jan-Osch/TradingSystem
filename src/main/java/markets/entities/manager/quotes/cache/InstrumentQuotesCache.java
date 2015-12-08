package markets.entities.manager.quotes.cache;

import markets.entities.instrument.Instrument;
import markets.entities.record.Record;

import java.util.HashMap;
import java.util.Map;

public class InstrumentQuotesCache {
    private Map<Instrument, Record> instrumentToRecordMap;

    public InstrumentQuotesCache() {
        this.instrumentToRecordMap = new HashMap<Instrument, Record>();
    }

    private void insertRecord(Record record) {
        this.instrumentToRecordMap.put(record.getInstrument(), record);
    }

    public Record getRecordForInstrument(Instrument instrument) {
        if (!this.instrumentToRecordMap.containsKey(instrument)) {
            throw new IllegalArgumentException();
        }
        return this.instrumentToRecordMap.get(instrument);
    }

    public void insertUpdateRecord(Record record) {
        if (!this.instrumentToRecordMap.containsKey(record.getInstrument())) {
            this.insertRecord(record);
        } else if (this.getRecordForInstrument(record.getInstrument()).compareTo(record) != 0) {
            this.insertRecord(record);
        }
    }

    public Iterable<Record> getRecords() {
        return this.instrumentToRecordMap.values();
    }
}
