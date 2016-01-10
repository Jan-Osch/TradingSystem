package com.bubble.persistance;


import com.bubble.markets.entities.record.HistoricalStockRecord;

import java.util.Date;
import java.util.UUID;

public interface HistoricalStockRecordGateWay {
    void saveHistoricalStockRecord(HistoricalStockRecord historicalStockRecord);

    Iterable<HistoricalStockRecord> getHistoricalStockRecordForPeriod(UUID instrumentUuid, UUID marketUuid, Date startDate, Date endDate);
}
