package com.bubble.markets.entities.instrument;

import java.util.UUID;

public class Index extends Instrument {

    public Index(UUID uuid, UUID marketUuid, String codeName, String fullName) {
        super(uuid, marketUuid, codeName, fullName, InstrumentType.INDEX);
    }
}
