package markets.entities.market;

import markets.entities.instrument.Instrument;
import markets.entities.record.Record;
import markets.exceptions.InstrumentUuidNotFoundException;

import java.util.*;

public class Market {
    private String name;
    private UUID uuid;
    private Map<UUID, Instrument> uuidInstrumentMap;
    private Map<UUID, Record> uuidRecordMap;
    private Set<String> codeSet;

    public Market(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
        this.uuidRecordMap = new HashMap<>();
        this.uuidInstrumentMap = new HashMap<>();
        this.codeSet = new HashSet<String>();
    }

    public Market(String name) {
        this(name, UUID.randomUUID());
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void pushRecord(Record record) {
        this.uuidRecordMap.put(record.getInstrumentUuid(), record);
    }

    public void addInstrument(Instrument instrument) {
        this.uuidInstrumentMap.put(instrument.getUuid(), instrument);
        this.codeSet.add(instrument.getCodeName());
    }

    public boolean isInstrumentOnMakret(Instrument instrument) {
        return this.uuidInstrumentMap.values().contains(instrument);
    }

    public boolean isInstrumentCodeOnMarket(Instrument instrument) {
        return this.codeSet.contains(instrument.getCodeName());
    }

    public boolean isCodeOnMarket(String code) {
        return this.codeSet.contains(code);
    }

    public boolean isInstrumentUuidOnTheMarket(UUID instrumentUuid) {
        return this.uuidInstrumentMap.containsKey(instrumentUuid);
    }

    public Record getCurrentRecord(UUID uuid) throws InstrumentUuidNotFoundException {
        Record result = this.uuidRecordMap.get(uuid);
        if (result != null) {
            return result;
        }
        throw new InstrumentUuidNotFoundException();
    }

    public Iterable<Record> getAllCurrentRecords() {
        return this.uuidRecordMap.values();
    }

    public Iterable<Instrument> getAllInstruments() {
        return this.uuidInstrumentMap.values();
    }

    public Instrument getInstrumentByUuid(UUID instrumentUuid) throws InstrumentUuidNotFoundException {
        if (this.isInstrumentUuidOnTheMarket(instrumentUuid)) {
            return this.uuidInstrumentMap.get(instrumentUuid);
        }
        throw new InstrumentUuidNotFoundException();
    }
}

