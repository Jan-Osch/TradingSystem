package markets.entities.record;

import markets.entities.instrument.Instrument;
import database.PostgreSQLJDBC;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class HistoricalStockRecord extends Record {
    private BigDecimal volumeInShares;
    private BigDecimal openingValue;
    private BigDecimal minimumValue;
    private float valueChangePercentage;
    private int numberOfTransactions;

    public HistoricalStockRecord(UUID uuid, Instrument instrument, Date date, BigDecimal volumeInShares, BigDecimal openingValue, BigDecimal closingValue, BigDecimal minimumValue, float valueChangePercentage, int numberOfTransactions) {
        super(instrument, uuid, date);
        this.volumeInShares = volumeInShares;
        this.openingValue = openingValue;
        this.minimumValue = minimumValue;
        this.valueChangePercentage = valueChangePercentage;
        this.numberOfTransactions = numberOfTransactions;
    }


    public BigDecimal getVolumeInShares() {
        return volumeInShares;
    }

    public BigDecimal getOpeningValue() {
        return openingValue;
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
