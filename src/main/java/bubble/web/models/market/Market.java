package bubble.web.models.market;

import bubble.web.models.instrument.InstrumentType;
import bubble.web.models.instrument.manager.InstrumentManager;

import java.util.HashMap;
import java.util.Map;

public class Market {
    private String name;
    private Map<InstrumentType, InstrumentManager> instrumentTypeInstrumentManagerMap;

    public Market(String name) {
        this.name = name;
        this.instrumentTypeInstrumentManagerMap = new HashMap<InstrumentType, InstrumentManager>();
    }

    public void addInstrumentToMarket(InstrumentType instrumentType, InstrumentManager instrumentManager){
        if(this.instrumentTypeInstrumentManagerMap.containsKey(instrumentType)){
            throw new IllegalArgumentException("Instrument already on the market!");
        }
        this.instrumentTypeInstrumentManagerMap.put(instrumentType, instrumentManager);
    }
}

