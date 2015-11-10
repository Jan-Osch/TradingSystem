package bubble.web.models.record;

import bubble.web.models.instrument.Instrument;

import java.math.BigDecimal;
import java.util.Date;

public class StockRecord extends Record {
    private Instrument instrument;
    private BigDecimal value;
    private BigDecimal volume;
    private Date dateCreated;

    public StockRecord(Instrument instrument, BigDecimal value, BigDecimal volume) {
        this.instrument = instrument;
        this.value = value;
        this.volume = volume;
        this.dateCreated = new Date();
    }

    @Override
    public Instrument getInstrument() {
        return this.instrument;
    }

    @Override
    Date getDateCreated() {
        return this.dateCreated;
    }

    public BigDecimal getValue() {
        return value;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    @Override
    public String getStringRepresentation() {
        return "Value: " + this.value + " Volume: " + this.volume;
    }

    @Override
    public int compareTo(Object o) {

        try {
            StockRecord rec = (StockRecord) o;
            if (!this.getInstrument().equals(rec.getInstrument())) {
                return this.getInstrument().getCodeName().compareTo(rec.getInstrument().getCodeName());
            }
            if (this.getValue().compareTo(rec.getValue()) != 0) {
                return this.getValue().compareTo(rec.getValue());
            }
            return this.getVolume().compareTo(rec.getVolume());
        } catch (ClassCastException e) {
            return -1;
        }
    }
}
