package com.bubble.markets.entities.instrument;

import java.util.UUID;

public class Stock extends Instrument {
    public Stock(UUID uuid, UUID marketUuid, String codeName, String fullName) {
        super(uuid, marketUuid, codeName, fullName, InstrumentType.STOCK);
    }
}
