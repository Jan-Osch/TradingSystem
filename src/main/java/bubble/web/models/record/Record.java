package bubble.web.models.record;

import bubble.web.models.instrument.Instrument;

import java.util.Date;

public abstract class Record implements Comparable {
    public abstract Instrument getInstrument();

    abstract Date getDateCreated();

    public abstract String getStringRepresentation();

    public abstract void add(Record record);
    public abstract void divideByInteger(int number);
    public abstract Record copy();
    public abstract void saveToDatabase(String tableName);
    public abstract void printComparison(Record record);
}