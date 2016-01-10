package com.bubble.markets.entities.record;


import com.bubble.markets.entities.instrument.Instrument;

import java.math.BigDecimal;
import java.util.Date;

public class StockRecord extends Record {
    private BigDecimal value;

    public StockRecord(Instrument instrument, BigDecimal value, Date date) {
        super(instrument, date);
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
