package markets.interactors;

import com.google.common.collect.Iterables;
import commons.InteractorTransactionWithException;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.entities.record.HistoricalStockRecord;
import markets.entities.record.Record;
import markets.entities.record.StockRecord;
import markets.exceptions.MarketNotFoundException;
import persistance.EntityGateWayManager;
import persistance.HistoricalStockRecordGateWay;

import java.util.UUID;

public class PushRecordTransaction implements InteractorTransactionWithException {
    private UUID marketUuid;
    private Record record;

    public PushRecordTransaction(UUID marketUuid, Record record) {
        this.marketUuid = marketUuid;
        this.record = record;
    }

    @Override
    public void execute() throws MarketNotFoundException {
        Market market = MarketManager.getMarketByUuid(marketUuid);
        if (this.record instanceof HistoricalStockRecord) {
            pushHistoricalRecord();
        } else if (this.record instanceof StockRecord) {
            market.pushRecord(record);
        }
    }

    private void pushHistoricalRecord() {
        HistoricalStockRecordGateWay historicalStockRecordGateWay = EntityGateWayManager.getHistoricalStockRecordGateWay();
        HistoricalStockRecord historicalStockRecord = (HistoricalStockRecord) this.record;
        Iterable<HistoricalStockRecord> previous = historicalStockRecordGateWay.getHistoricalStockRecordForPeriod(
                historicalStockRecord.getInstrumentUuid(),
                this.marketUuid,
                historicalStockRecord.getDate(),
                historicalStockRecord.getDate());
        if (Iterables.size(previous) == 0) {
            historicalStockRecordGateWay.saveHistoricalStockRecord((HistoricalStockRecord) this.record);
        }
    }
}
