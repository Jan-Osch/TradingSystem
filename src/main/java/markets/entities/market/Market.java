package markets.entities.market;

import markets.entities.instrument.InstrumentType;
import markets.entities.manager.InstrumentManager;

import java.util.HashMap;
import java.util.Map;

public class Market {
    private String name;
    private Map<InstrumentType, InstrumentManager> instrumentTypeInstrumentManagerMap;

    public Market(String name) {
        this.name = name;
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

    private class InstrumentAlreadyOnMarket extends Throwable {
    }
}

