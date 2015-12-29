package markets.interactors;

import commons.InteractorTransactionWithException;
import markets.entities.instrument.Index;
import markets.entities.instrument.Instrument;
import markets.entities.instrument.InstrumentType;
import markets.entities.instrument.Stock;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.exceptions.InstrumentCodeAlreadyOnMarketException;
import markets.exceptions.MarketNotFoundException;
import persistance.EntityGateWayManager;
import persistance.IndexGateWay;
import persistance.StockGateWay;

public class AddInstrumentTransaction implements InteractorTransactionWithException {
    private Instrument instrument;

    public AddInstrumentTransaction(Instrument instrument) {
        this.instrument = instrument;
    }

    @Override
    public void execute() throws MarketNotFoundException, InstrumentCodeAlreadyOnMarketException {
        Market market = MarketManager.getMarketByUuid(instrument.getMarketUuid());
        loadInstruments();
        validateInstrumentCode();
        if (instrument.getInstrumentType() == InstrumentType.STOCK) {
            addStock();
        } else if (instrument.getInstrumentType() == InstrumentType.INDEX) {
            addIndex();
        }
        market.addInstrument(instrument);
    }

    private void addIndex() {
        IndexGateWay indexGateWay = EntityGateWayManager.getIndexGateWay();
        indexGateWay.saveIndex((Index) instrument);

    }

    private void validateInstrumentCode() throws MarketNotFoundException, InstrumentCodeAlreadyOnMarketException {
        Market market = MarketManager.getMarketByUuid(instrument.getMarketUuid());
        if (market.isInstrumentCodeOnMarket(instrument)) {
            throw new InstrumentCodeAlreadyOnMarketException();
        }
    }

    private void loadInstruments() throws MarketNotFoundException {
        LoadInstrumentsForMarketTransaction loadInstrumentsForMarketTransaction = new LoadInstrumentsForMarketTransaction(instrument.getMarketUuid());
        loadInstrumentsForMarketTransaction.execute();
    }

    private void addStock() throws MarketNotFoundException {
        StockGateWay stockGateWay = EntityGateWayManager.getStockGateWay();
        stockGateWay.saveStock((Stock) instrument);
    }
}
