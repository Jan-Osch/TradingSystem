package markets.interactors;

import commons.InteractorTransactionWithResultAndException;
import markets.entities.instrument.Instrument;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.exceptions.MarketNotFoundException;

import java.util.UUID;

public class GetAllInstrumentsForMarket implements InteractorTransactionWithResultAndException {

    private UUID marketUUid;

    public GetAllInstrumentsForMarket(UUID marketUUid) {
        this.marketUUid = marketUUid;
    }

    @Override
    public Iterable<Instrument> execute() throws MarketNotFoundException {
        Market market = MarketManager.getMarketByUuid(marketUUid);
        return market.getAllInstruments();
    }
}
