package models;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Janusz
 */
public class Record {
    private Index index;
    private BigDecimal value;
    private Date date;

    public Record(Index index, BigDecimal value, Date date) {
        this.index = index;
        this.value = value;
        this.date = date;
    }
}
