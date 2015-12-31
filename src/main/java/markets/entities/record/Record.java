package markets.entities.record;

import markets.entities.instrument.Instrument;

import java.util.Date;
import java.util.UUID;

public abstract class Record {
    private transient final Instrument instrument;
    private final UUID instrumentUuid;
    private final Date date;

    public Record(Instrument instrument, Date date) {
        this.instrument = instrument;
        this.instrumentUuid = instrument.getUuid();
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