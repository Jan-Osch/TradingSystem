package markets.entities.record;

import markets.entities.instrument.Instrument;
import markets.entities.instrument.Stock;
import database.PostgreSQLJDBC;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;

public class StockRecord extends Record {
    private Instrument instrument;
    private BigDecimal value;

    public StockRecord(Instrument instrument, BigDecimal value, Date date) {
        super(instrument, date);
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
