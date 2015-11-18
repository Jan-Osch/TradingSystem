package bubble.web.models.instrument.quotes.container;

import bubble.web.models.record.Record;

public class InstrumentQuotesHistoricalContainer extends InstrumentQuotesContainer{

    public InstrumentQuotesHistoricalContainer(String databaseTableName) {
        super(databaseTableName);
    }

    @Override
    public void pushRecordToContainer(Record record) {
        saveToDatabase(record);
    }
}
