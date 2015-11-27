package bubble.web.instruments.instrument.quotes.container;

import bubble.web.instruments.instrument.Instrument;
import bubble.web.instruments.record.Record;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InstrumentQuotesIntervalContainer extends InstrumentQuotesContainer {
    private InstrumentQuotesIntervalContainer pipelineMeanStockQuoteHolder;
    private Map<Instrument, Record> previousRecords;
    private Map<Instrument, Record> currentRecords;
    private Map<Instrument, Integer> numberOfChanges;
    private int numberOfIntervals;
    private int currentIteration;

    public InstrumentQuotesIntervalContainer(int numberOfIntervals, InstrumentQuotesIntervalContainer pipelineMeanStockQuoteHolder, String databaseTableName) {
        super(databaseTableName);
        this.numberOfIntervals = numberOfIntervals;
        this.pipelineMeanStockQuoteHolder = pipelineMeanStockQuoteHolder;
        this.currentIteration = 0;
        this.currentRecords = new ConcurrentHashMap<Instrument, Record>();
        this.previousRecords = new ConcurrentHashMap<Instrument, Record>();
        this.numberOfChanges = new ConcurrentHashMap<Instrument, Integer>();
    }

    public InstrumentQuotesIntervalContainer(int numberOfIntervals, String databaseTableName) {
        this(numberOfIntervals, null, databaseTableName);
    }

    @Override
    public void pushRecordToContainer(Record record) {
        if (!this.currentRecords.containsKey(record.getInstrument())) {
            this.currentRecords.put(record.getInstrument(), record.getRecordCopy());
            this.numberOfChanges.put(record.getInstrument(), 1);
        } else {
            this.currentRecords.get(record.getInstrument()).addValueOfAnotherRecord(record);
            this.numberOfChanges.computeIfPresent(record.getInstrument(), (key, value) -> value + 1);
        }
    }

    private void propagateChangesToNextContainer() {
        if (this.pipelineMeanStockQuoteHolder != null) {
            this.pipelineMeanStockQuoteHolder.propagateRecords(this.currentRecords.values());
        }
    }

    private void processRecordsOnIntervalEnd() {
        int numberOfChangedRecords = 0;
        for (Record currentRecord : this.currentRecords.values()) {
            currentRecord.divideByInteger(this.numberOfChanges.get(currentRecord.getInstrument()));
            if (this.previousRecords.containsKey(currentRecord.getInstrument())
                    && this.previousRecords.get(currentRecord.getInstrument()).compareTo(currentRecord) == 0) {
                this.currentRecords.remove(currentRecord.getInstrument());
            } else {
                if(this.previousRecords.containsKey(currentRecord.getInstrument())){
                    this.previousRecords.get(currentRecord.getInstrument()).printComparison(currentRecord);
                }
                numberOfChangedRecords++;
            }
        }
        System.out.format("InstrumentQuotesIntervalContainer: databaseName: %s number of records changed: %d\n", this.databaseTableName, numberOfChangedRecords);
    }

    private void saveChangedRecordsToDatabase() {
        for (Record currentRecord : this.currentRecords.values()) {
            this.saveToDatabase(currentRecord.getRecordCopy());
        }
    }

    private void moveCurrentToPrevious() {
        for (Record currentRecord : this.currentRecords.values()) {
            this.previousRecords.put(currentRecord.getInstrument(), currentRecord);
            this.currentRecords.remove(currentRecord.getInstrument());
        }
    }

    public void propagateRecords(Iterable<Record> records) {
        this.currentIteration++;
        for (Record record : records) {
            this.pushRecordToContainer(record);
        }
        if (this.currentIteration == this.numberOfIntervals) {
            finishIteration();
        }
    }

    private void finishIteration() {
        this.processRecordsOnIntervalEnd();
        this.saveChangedRecordsToDatabase();
        this.propagateChangesToNextContainer();
        this.moveCurrentToPrevious();
        this.currentIteration = 0;
    }
}