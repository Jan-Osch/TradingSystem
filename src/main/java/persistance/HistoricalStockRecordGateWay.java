package persistance;

import markets.entities.record.HistoricalStockRecord;

public interface HistoricalStockRecordGateWay {
    void saveHistoricalStockRecord(HistoricalStockRecord historicalStockRecord);
}
