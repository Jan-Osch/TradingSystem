package markets.interactors;

import commons.InteractorTransaction;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.entities.record.Record;
import markets.entities.record.StockRecord;
import markets.exceptions.MarketNotFoundException;
import persistance.CurrentStockRecordGateWay;
import persistance.EntityGateWayManager;

import java.util.UUID;

public class SaveCurrentRecordsToDatabaseTransaction implements InteractorTransaction {
    private UUID marketUUid;

    public SaveCurrentRecordsToDatabaseTransaction(UUID marketUUid) {
        this.marketUUid = marketUUid;
    }

    @Override
    public void execute() {
        try {
            Market market = MarketManager.getMarketByUuid(this.marketUUid);
            Iterable<Record> stockRecords = market.getAllCurrentRecords();
            CurrentStockRecordGateWay stockRecordGateWay = EntityGateWayManager.getCurrentStockRecordGateWay();
            for(Record record: stockRecords){
                stockRecordGateWay.save((StockRecord) record);
            }
        } catch (MarketNotFoundException e) {
            e.printStackTrace();
        }
    }
}
