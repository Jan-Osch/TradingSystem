package markets.entities.market;

import markets.entities.instrument.InstrumentType;
import markets.entities.manager.InstrumentManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Market {
    private String name;

    private UUID uuid;

    public Map<InstrumentType, InstrumentManager> instrumentTypeInstrumentManagerMap;
    public UUID getUuid() {
        return uuid;
    }
    public String getName() {
        return name;
    }

    public Market(String name) {
        this(name, UUID.randomUUID());
    }

    public Market(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
        this.instrumentTypeInstrumentManagerMap = new HashMap<InstrumentType, InstrumentManager>();
    }

    public void addInstrumentToMarket(InstrumentType instrumentType, InstrumentManager instrumentManager) throws InstrumentAlreadyOnMarket {
        if(isInstrumentAlreadyOnMarket(instrumentType)){
            throw new InstrumentAlreadyOnMarket();
        }
        this.instrumentTypeInstrumentManagerMap.put(instrumentType, instrumentManager);
    }

    public Iterable<InstrumentType> getTrackedInstruments(){
        return this.instrumentTypeInstrumentManagerMap.keySet();
    }

    private boolean isInstrumentAlreadyOnMarket(InstrumentType instrumentType) {
        return this.instrumentTypeInstrumentManagerMap.containsKey(instrumentType);
    }

    public class InstrumentAlreadyOnMarket extends Throwable {
    }
}

