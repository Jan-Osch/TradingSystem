package bubble.web.instruments.instrument.quotes.container;

import bubble.web.instruments.record.Record;

public class InstrumentQuotesHistoricalContainer extends InstrumentQuotesContainer{

    public InstrumentQuotesHistoricalContainer(String databaseTableName) {
        super(databaseTableName);
    }

    @Override
    public void pushRecordToContainer(Record record) {
        saveToDatabase(record);
    }
}
