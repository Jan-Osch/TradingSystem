package bubble.web.models.instrument.quotes.interval.container;

import bubble.web.models.instrument.Instrument;
import bubble.web.models.record.Record;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InstrumentQuotesIntervalContainer {
    private InstrumentQuotesIntervalContainer pipelineMeanStockQuoteHolder;
    private Map<Instrument, Record> previousRecords;
    private Map<Instrument, Record> currentRecords;
    private int numberOfIntervals;
    private int currentIteration;
    private String name;

    public InstrumentQuotesIntervalContainer(int numberOfIntervals, InstrumentQuotesIntervalContainer pipelineMeanStockQuoteHolder, String name) {
        this.numberOfIntervals = numberOfIntervals;
        this.pipelineMeanStockQuoteHolder = pipelineMeanStockQuoteHolder;
        this.currentIteration = 0;
        this.name = name;
        this.currentRecords = new ConcurrentHashMap<Instrument, Record>();
        this.previousRecords = new ConcurrentHashMap<Instrument, Record>();
    }

    public InstrumentQuotesIntervalContainer(int numberOfIntervals, String name) {
        this(numberOfIntervals, null, name);
    }

    public void addRecord(Record record) {
        if (!this.currentRecords.containsKey(record.getInstrument())) {
            this.currentRecords.put(record.getInstrument(), record.copy());
        } else {
            this.currentRecords.get(record.getInstrument()).add(record);
        }
    }

    private void propagateChangesToNextContainer() {
        if (this.pipelineMeanStockQuoteHolder != null) {
            this.pipelineMeanStockQuoteHolder.propagateRecords(this.currentRecords.values());
        }
    }

    private void processRecordsOnIntervalEnd() {
        for (Record currentRecord : this.currentRecords.values()) {
            currentRecord.divideByInteger(this.numberOfIntervals);
            if (this.previousRecords.containsKey(currentRecord.getInstrument())
                    && this.previousRecords.get(currentRecord.getInstrument()).compareTo(currentRecord) == 0) {
                this.currentRecords.remove(currentRecord.getInstrument());
            }
        }
    }

    private void saveChangedRecordsToDatabase() {
        for (Record currentRecord : this.currentRecords.values()) {
            this.saveToDatabase(currentRecord.copy());
        }
    }

    private void moveCurrentToPrevious() {
        for (Record currentRecord : this.currentRecords.values()) {
            this.previousRecords.put(currentRecord.getInstrument(), currentRecord);
        }
    }

    private void saveToDatabase(Record record) {
//        System.out.println(this.name + " Saving record to database " + record.toString());
    }

    public void propagateRecords(Iterable<Record> records) {
        System.out.println(this.name + " adds records");
        this.currentIteration++;
        for (Record record : records) {
            this.addRecord(record);
        }
        if (this.currentIteration == this.numberOfIntervals) {
            this.processRecordsOnIntervalEnd();
            this.saveChangedRecordsToDatabase();
            this.propagateChangesToNextContainer();
            this.moveCurrentToPrevious();
            this.currentIteration = 0;
        }

    }
}