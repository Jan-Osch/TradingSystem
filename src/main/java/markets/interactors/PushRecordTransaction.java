package markets.interactors;

import commons.InteractorTransaction;
import markets.entities.instrument.InstrumentType;
import markets.entities.manager.StockManager;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.entities.record.Record;
import markets.entities.record.StockRecord;

import java.util.UUID;

public class PushRecordTransaction implements InteractorTransaction {
    private UUID marketUuid;
    private Record record;
    private Market market;

    @Override
    public void execute() {
        loadMarket();
        if(this.record instanceof StockRecord){
            this.pushStockRecord();
        }

    }

    private void loadMarket() {
        MarketManager.getInstance();
        this.market =  MarketManager.marketHashMap.get(marketUuid);
    }

    private void pushStockRecord() {
        StockManager stockManager = (StockManager) this.market.instrumentTypeInstrumentManagerMap.get(InstrumentType.STOCK);
    }
}
