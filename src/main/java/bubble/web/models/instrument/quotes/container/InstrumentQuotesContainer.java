package bubble.web.models.instrument.quotes.container;

import bubble.web.models.record.Record;

public abstract class InstrumentQuotesContainer {

    String databaseTableName;

    public InstrumentQuotesContainer(String databaseTableName) {
        this.databaseTableName = databaseTableName;
    }

    public abstract void pushRecordToContainer(Record record);

    void saveToDatabase(Record record) {
        record.saveToDatabase(this.databaseTableName);
    }
}
