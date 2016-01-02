package markets.interactors;

import markets.entities.instrument.Instrument;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.entities.record.StockRecord;
import markets.exceptions.InstrumentUuidNotFoundException;
import markets.exceptions.MarketNotFoundException;
import persistance.EntityGateWayManager;
import persistance.MarketGateWay;

import java.math.BigDecimal;
import java.util.UUID;


public class MarketsInteractor {
    private static MarketGateWay marketGateWay = EntityGateWayManager.getMarketGateWay();

    public static Iterable<Market> getAllMarkets() {
        Iterable<Market> allMarkets = marketGateWay.getAllMarkets();
        for (Market market : allMarkets) {
            MarketManager.addMarket(market);
        }
        return MarketManager.getAllMarkets();
    }

    public static BigDecimal getCurrentStockValue(UUID instrumentUUID) throws MarketNotFoundException, InstrumentUuidNotFoundException {
        Market market = getMarketForInstrument(instrumentUUID);
        StockRecord currentRecord = (StockRecord) market.getCurrentRecord(instrumentUUID);
        return currentRecord.getValue();
    }

    private static Market getMarketForInstrument(UUID instrumentUUID) throws MarketNotFoundException, InstrumentUuidNotFoundException {
        return MarketManager.getMarketByUuid(getMarketUuidForInstrument(instrumentUUID));
    }

    private static UUID getMarketUuidForInstrument(UUID instrumentUUID) throws InstrumentUuidNotFoundException {
        for (Market market : getAllMarkets()) {
            if (market.isInstrumentUuidOnTheMarket(instrumentUUID)) {
                return market.getUuid();
            }
        }
        throw new InstrumentUuidNotFoundException();
    }

    public static Instrument getInstrument(UUID marketUuid, UUID instrumentUUID) throws MarketNotFoundException, InstrumentUuidNotFoundException {
        Market market = MarketManager.getMarketByUuid(marketUuid);
        return market.getInstrumentByUuid(instrumentUUID);
    }
}
