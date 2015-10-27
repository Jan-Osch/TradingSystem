package models;

import commons.IdHelper;

import java.math.BigDecimal;
import java.util.Date;

public class Record {
    private final int id;
    private final Index index;
    private final BigDecimal value;
    private final Date date;

    public Record(Index index, BigDecimal value) {
        this.id = IdHelper.getInstance().getNewId();
        this.index = index;
        this.value = value;
        this.date = new Date();
    }

    public BigDecimal getValue() {
        return value;
    }
}
