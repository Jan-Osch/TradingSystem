package bubble.web.models.record;

import bubble.web.models.instrument.Instrument;

import java.util.Date;

public abstract class Record implements Comparable {
    public abstract Instrument getInstrument();

    abstract Date getDateCreated();

    public abstract String getStringRepresentation();
}