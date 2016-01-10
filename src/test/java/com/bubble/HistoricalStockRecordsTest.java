import database.PostgresHistoricalStockRecordDao;
import markets.entities.instrument.Stock;
import markets.entities.record.HistoricalStockRecord;
import persistance.EntityGateWayManager;
import persistance.HistoricalStockRecordGateWay;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class HistoricalStockRecordsTest {

    public static void main(String[] args) {
        PostgresHistoricalStockRecordDao postgresHistoricalStockRecordDao = new PostgresHistoricalStockRecordDao();
        EntityGateWayManager.setHistoricalStockRecordGateWay(postgresHistoricalStockRecordDao);

        HistoricalStockRecord hist = new HistoricalStockRecord(
                new Stock(UUID.randomUUID(), UUID.randomUUID(), "SIEKA","SPIEKA"),
                new Date(),
                BigDecimal.TEN,
                BigDecimal.valueOf(123l),
                BigDecimal.ONE,
                BigDecimal.ZERO,
                0.1f,123);

        HistoricalStockRecordGateWay historicalStockRecordGateWay = EntityGateWayManager.getHistoricalStockRecordGateWay();

        historicalStockRecordGateWay.saveHistoricalStockRecord(hist);


    }
}
