package persistance;

import markets.entities.record.StockRecord;

import java.util.Date;
import java.util.UUID;

public interface CurrentStockRecordGateWay {
    void save(StockRecord stockRecord);

    Iterable<StockRecord> getCurrentRecordsForPeriod(UUID instrumentUuid, UUID marketUuid, Date startDate, Date endDate);
}
