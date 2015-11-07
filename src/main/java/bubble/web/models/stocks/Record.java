package bubble.web.models.stocks;

import java.math.BigDecimal;

public class Record {
    private final BigDecimal value;
    private final String stockName;
    private final BigDecimal volume;

    public Record(BigDecimal value, String stockName, BigDecimal volume) {
        this.value = value;
        this.stockName = stockName;
        this.volume = volume;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getStockName() {
        return stockName;
    }

    public BigDecimal getVolume() {
        return volume;
    }
}