package com.bubble.markets.entities.instrument;


import java.util.UUID;

public abstract class Instrument {
    private UUID uuid;
    private UUID marketUuid;
    private String codeName;
    private String fullName;
    private InstrumentType instrumentType;

    public Instrument(UUID uuid, UUID marketUuid, String codeName, String fullName, InstrumentType instrumentType) {
        this.uuid = uuid;
        this.marketUuid = marketUuid;
        this.codeName = codeName;
        this.fullName = fullName;
        this.instrumentType = instrumentType;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getMarketUuid() {
        return marketUuid;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getFullName() {
        return fullName;
    }

    public InstrumentType getInstrumentType() {
        return instrumentType;
    }
}
