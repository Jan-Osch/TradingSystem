package bubble.web.models.record;

import bubble.web.models.instrument.Instrument;
import bubble.web.models.instrument.Stock;
import bubble.web.postgresSQLJDBC.PostgreSQLJDBC;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

public class StockRecord extends Record {
    private Instrument instrument;
    private BigDecimal value;
    private Date dateCreated;
    static DecimalFormat decimalFormat = new DecimalFormat("+#,##0.00;-#");

    public StockRecord(Instrument instrument, BigDecimal value, Date date) {
        this.instrument = instrument;
        this.value = value;
        this.dateCreated = date;
    }

    public StockRecord(Stock stock, BigDecimal value) {
        this(stock, value, new Date());
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

    @Override
    public String getStringRepresentation() {
        return "Code: " + this.instrument.getCodeName() + " Value: " + this.value;
    }

    @Override
    public void addValueOfAnotherRecord(Record record) {
        StockRecord stockRecord = (StockRecord) record;
        this.value = this.value.add(stockRecord.getValue());
    }

    @Override
    public void divideByInteger(int number) {
        this.value = this.value.divideToIntegralValue(BigDecimal.valueOf(number));
    }

    @Override
    public Record getRecordCopy() {
        return new StockRecord(this.instrument, this.value, this.getDateCreated());
    }

    @Override
    public int compareTo(Object o) {

        try {
            StockRecord rec = (StockRecord) o;
            if (!this.getInstrument().equals(rec.getInstrument())) {
                return this.getInstrument().getCodeName().compareTo(rec.getInstrument().getCodeName());
            }
            return this.getValue().compareTo(rec.getValue());
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
            System.out.format("StockRecord - code: %s value change: %s \n",
                    this.instrument.getCodeName(),
                    decimalFormat.format(anotherRecord.getValue().subtract(this.getValue()))
            );
        }
    }
}
