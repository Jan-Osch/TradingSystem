package markets.entities.manager.quotes.container;

import markets.entities.record.Record;

public class InstrumentQuotesHistoricalContainer extends InstrumentQuotesContainer{

    public InstrumentQuotesHistoricalContainer(String databaseTableName) {
        super(databaseTableName);
    }

    @Override
    public void pushRecordToContainer(Record record) {
        saveToDatabase(record);
    }
}
