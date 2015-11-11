package bubble.web.models.instrument.quotes.cache;

import bubble.web.models.instrument.Instrument;
import bubble.web.models.record.Record;

import java.util.HashMap;
import java.util.Iterator;
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
//            System.out.println("New instrument - Type: " + record.getInstrument().getInstrumentType()
//                    + " Code: " + record.getInstrument().getCodeName()
//                    + " - " + record.getStringRepresentation());
            this.insertRecord(record);
        } else if (this.getRecordForInstrument(record.getInstrument()).compareTo(record) != 0) {
//            System.out.println("Record changed - Instrument: " + record.getInstrument().getInstrumentType()
//                    + " Code: " + record.getInstrument().getCodeName()
//                    + " - " + record.getStringRepresentation());
            this.insertRecord(record);
        } else {
//            System.out.println("No change - Instrument: " + record.getInstrument().getInstrumentType()
//                    + " Code: " + record.getInstrument().getCodeName());
        }
    }
    public Iterable<Record> getRecords(){
        return this.instrumentToRecordMap.values();
    }
}
