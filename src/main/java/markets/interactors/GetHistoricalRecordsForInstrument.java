package markets.interactors;

import commons.InteractorTransactionWithResultAndException;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.exceptions.InstrumentUuidNotFoundException;
import markets.exceptions.MarketNotFoundException;
import persistance.EntityGateWayManager;
import persistance.HistoricalStockRecordGateWay;

import java.util.Date;
import java.util.UUID;

public class GetHistoricalRecordsForInstrument implements InteractorTransactionWithResultAndException {

    private final UUID instrumentUuid;
    private final UUID marketUuid;
    private final Date startDate;
    private final Date endDate;

    public GetHistoricalRecordsForInstrument(UUID instrumentUuid, UUID marketUuid, Date startDate, Date endDate) {
        this.instrumentUuid = instrumentUuid;
        this.marketUuid = marketUuid;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public Object execute() throws MarketNotFoundException, InstrumentUuidNotFoundException {
        Market market = MarketManager.getMarketByUuid(this.marketUuid);
        if (!market.isInstrumentUuidOnTheMarket(this.instrumentUuid)) {
            throw new InstrumentUuidNotFoundException();
        }

        HistoricalStockRecordGateWay historicalStockRecordGateWay = EntityGateWayManager.getHistoricalStockRecordGateWay();
        return historicalStockRecordGateWay.getHistoricalStockRecordForPeriod(this.instrumentUuid,
                this.marketUuid,
                this.startDate,
                this.endDate);
    }
}
