package bubble.web.instruments.record;

import bubble.web.instruments.instrument.Instrument;

import java.util.Date;

public abstract class Record implements Comparable {
    public abstract Instrument getInstrument();

    abstract Date getDateCreated();

    public abstract String getStringRepresentation();

    public abstract void addValueOfAnotherRecord(Record record);
    public abstract void divideByInteger(int number);
    public abstract Record getRecordCopy();
    public abstract void saveToDatabase(String tableName);
    public abstract void printComparison(Record record);
}