package markets.interactors;

import commons.InteractorTransactionWithResultAndException;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.entities.record.Record;
import markets.exceptions.InstrumentUuidNotFoundException;
import markets.exceptions.MarketNotFoundException;

import java.util.UUID;

public class GetCurrentRecordForInstrumentTransaction implements InteractorTransactionWithResultAndException {
    private UUID instrumentUUID;
    private UUID marketUUID;

    public GetCurrentRecordForInstrumentTransaction(UUID instrumentUUID, UUID marketUUID) {
        this.instrumentUUID = instrumentUUID;
        this.marketUUID = marketUUID;
    }

    @Override
    public Record execute() throws MarketNotFoundException, InstrumentUuidNotFoundException {
        Market market = MarketManager.getMarketByUuid(this.marketUUID);
        return market.getCurrentRecord(this.instrumentUUID);
    }
}
