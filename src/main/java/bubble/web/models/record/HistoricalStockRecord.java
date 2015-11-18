package bubble.web.models.record;

import bubble.web.models.instrument.Instrument;
import bubble.web.postgresSQLJDBC.PostgreSQLJDBC;

import java.math.BigDecimal;
import java.util.Date;

public class HistoricalStockRecord extends StockRecord {
    private BigDecimal volumeInShares;
    private BigDecimal openingValue;
    private BigDecimal closingValue;
    private BigDecimal minimumValue;
    private float valueChangePercentage;
    private int numberOfTransactions;

    public HistoricalStockRecord(Instrument instrument, BigDecimal value, Date date, BigDecimal volumeInShares, BigDecimal openingValue, BigDecimal closingValue, BigDecimal minimumValue, float valueChangePercentage, int numberOfTransactions) {
        super(instrument, closingValue, date);
        this.volumeInShares = volumeInShares;
        this.openingValue = openingValue;
        this.minimumValue = minimumValue;
        this.valueChangePercentage = valueChangePercentage;
        this.numberOfTransactions = numberOfTransactions;
    }

    @Override
    public void saveToDatabase(String tableName) {
        PostgreSQLJDBC.getInstance().saveHistoricalStockRecordToDatabase(tableName, this);
    }

    public BigDecimal getVolumeInShares() {
        return volumeInShares;
    }

    public BigDecimal getOpeningValue() {
        return openingValue;
    }

    public BigDecimal getClosingValue() {
        return closingValue;
    }

    public BigDecimal getMinimumValue() {
        return minimumValue;
    }

    public float getValueChangePercentage() {
        return valueChangePercentage;
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }
}
