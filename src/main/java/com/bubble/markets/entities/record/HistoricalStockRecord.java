package com.bubble.markets.entities.record;


import com.bubble.markets.entities.instrument.Instrument;

import java.math.BigDecimal;
import java.util.Date;

public class HistoricalStockRecord extends Record {
    private BigDecimal volumeInShares;
    private BigDecimal openingValue;
    private BigDecimal minimumValue;
    private BigDecimal closingValue;
    private float valueChangePercentage;
    private int numberOfTransactions;

    public HistoricalStockRecord(Instrument instrument, Date date, BigDecimal volumeInShares, BigDecimal openingValue, BigDecimal closingValue, BigDecimal minimumValue, float valueChangePercentage, int numberOfTransactions) {
        super(instrument, date);
        this.volumeInShares = volumeInShares;
        this.openingValue = openingValue;
        this.minimumValue = minimumValue;
        this.closingValue = closingValue;
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

    public BigDecimal getClosingValue() {
        return closingValue;
    }
}
