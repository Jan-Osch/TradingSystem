package markets.entities.record;

import markets.entities.instrument.Instrument;

import java.util.Date;
import java.util.UUID;

public abstract class Record {
    private final Instrument instrument;
    private final UUID instrumentUuid;
    private final Date date;

    public Record(Instrument instrument, UUID instrumentUuid, Date date) {
        this.instrument = instrument;
        this.instrumentUuid = instrumentUuid;
        this.date = date;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public UUID getInstrumentUuid() {
        return instrumentUuid;
    }

    public Date getDate() {
        return date;
    }
}