package markets.interactors;

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
            HistoricalStockRecordGateWay historicalStockRecordGateWay = EntityGateWayManager.getHistoricalStockRecordGateWay();
            historicalStockRecordGateWay.saveHistoricalStockRecord((HistoricalStockRecord) this.record);
        } else if (this.record instanceof StockRecord) {
            market.pushRecord(record);
        }
    }
}
