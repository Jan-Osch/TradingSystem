package markets.interactors;

import commons.InteractorTransactionWithResult;
import commons.InteractorTransactionWithResultAndException;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.entities.record.StockRecord;
import org.omg.CORBA.Current;
import persistance.CurrentStockRecordGateWay;
import persistance.EntityGateWayManager;

import java.util.Date;
import java.util.EmptyStackException;
import java.util.UUID;

public class GetCurrentRecordsForPeriodTransaction implements InteractorTransactionWithResult {

    private UUID instrumentUuid;
    private UUID marketUuid;
    private Date dateStart;
    private Date dateEnd;

    public GetCurrentRecordsForPeriodTransaction(UUID instrumentUuid, UUID marketUuid, Date dateStart, Date dateEnd) {
        this.instrumentUuid = instrumentUuid;
        this.marketUuid = marketUuid;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    @Override
    public Iterable<StockRecord> execute() {
        CurrentStockRecordGateWay currentStockRecordGateWay = EntityGateWayManager.getCurrentStockRecordGateWay();
        return currentStockRecordGateWay.getCurrentRecordsForPeriod(
                this.instrumentUuid,
                this.marketUuid,
                this.dateStart,
                this.dateEnd);
    }
}
