package models;

import java.math.BigDecimal;
import java.util.Date;

public class Record {
    private final Index index;
    private final BigDecimal value;
    private final Date date;

    public Record(Index index, BigDecimal value, Date date) {
        this.index = index;
        this.value = value;
        this.date = date;
    }
}
