package markets.interactors;

import commons.InteractorTransactionWithResult;
import commons.InteractorTransactionWithResultAndException;
import markets.entities.instrument.Instrument;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.exceptions.InstrumentUuidNotFoundException;
import markets.exceptions.MarketNotFoundException;

import java.util.UUID;

public class GetInstrumentTransaction implements InteractorTransactionWithResultAndException {

    private UUID instrumentUuid;
    private UUID marketUuid;

    public GetInstrumentTransaction(UUID instrumentUuid, UUID marketUuid) {
        this.instrumentUuid = instrumentUuid;
        this.marketUuid = marketUuid;
    }

    @Override
    public Instrument execute() throws MarketNotFoundException, InstrumentUuidNotFoundException {
        Market market = MarketManager.getMarketByUuid(this.marketUuid);
        return market.getInstrumentByUuid(instrumentUuid);
    }
}
