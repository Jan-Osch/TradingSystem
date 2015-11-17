package bubble.web.models.record;

import bubble.web.models.instrument.Instrument;
import bubble.web.models.instrument.Stock;
import bubble.web.postgresSQLJDBC.PostgreSQLJDBC;

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

    public StockRecord(Stock stock, int i, int i1) {
        this(stock, BigDecimal.valueOf(i), BigDecimal.valueOf(i1));
    }


    @Override
    public Instrument getInstrument() {
        return this.instrument;
    }

    @Override
    public Date getDateCreated() {
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
    public void add(Record record) {
        StockRecord stockRecord = (StockRecord) record;
        this.volume = this.volume.add(stockRecord.getVolume());
        this.value = this.value.add(stockRecord.getValue());
    }

    @Override
    public void divideByInteger(int number) {
        this.value = this.value.divideToIntegralValue(BigDecimal.valueOf(number));
        this.volume = this.volume.divideToIntegralValue(BigDecimal.valueOf(number));
    }

    @Override
    public Record copy() {
        return new StockRecord(this.instrument, this.value, this.volume);
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

    public void saveToDatabase(String tableName) {
        PostgreSQLJDBC.getInstance().saveStockRecordToDatabase(tableName, this);
    }

    @Override
    public void printComparison(Record record) {
        StockRecord anotherRecord;
        try {
            anotherRecord = (StockRecord) record;
        } catch (ClassCastException e) {
            return;
        }
        if (anotherRecord.getInstrument() == this.instrument) {
            System.out.format("StockRecord - code: %s value change: %s volume change: %s\n",
                    this.instrument.getCodeName(),
                    this.getValue().subtract(anotherRecord.getValue()).negate().toString(),
                    this.getVolume().subtract(anotherRecord.getVolume()).negate().toString());
        }
    }
}
