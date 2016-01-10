package com.bubble.markets.interactors;

import com.bubble.markets.entities.instrument.Index;
import com.bubble.markets.entities.instrument.Instrument;
import com.bubble.markets.entities.instrument.InstrumentType;
import com.bubble.markets.entities.instrument.Stock;
import com.bubble.markets.entities.market.Market;
import com.bubble.markets.entities.market.MarketManager;
import com.bubble.markets.entities.record.HistoricalStockRecord;
import com.bubble.markets.entities.record.Record;
import com.bubble.markets.entities.record.StockRecord;
import com.bubble.markets.exceptions.InstrumentCodeAlreadyOnMarketException;
import com.bubble.markets.exceptions.InstrumentUuidNotFoundException;
import com.bubble.markets.exceptions.MarketByThisNameAlreadyExistsException;
import com.bubble.markets.exceptions.MarketNotFoundException;
import com.bubble.persistance.*;
import com.google.common.collect.Iterables;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


public class MarketsInteractor {
    private static MarketGateWay marketGateWay = EntityGateWayManager.getMarketGateWay();
    private static StockGateWay stockGateWay = EntityGateWayManager.getStockGateWay();
    private static IndexGateWay indexGateWay = EntityGateWayManager.getIndexGateWay();
    private static HistoricalStockRecordGateWay historicalStockRecordGateWay = EntityGateWayManager.getHistoricalStockRecordGateWay();
    private static CurrentStockRecordGateWay currentStockRecordGateWay = EntityGateWayManager.getCurrentStockRecordGateWay();

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

    public static void addInstrument(Instrument instrument) throws MarketNotFoundException, InstrumentCodeAlreadyOnMarketException {
        loadInstrumentsForMarket(instrument.getMarketUuid());
        validateInstrumentCode(instrument);
        persistInstrument(instrument);
        Market market = MarketManager.getMarketByUuid(instrument.getMarketUuid());
        market.addInstrument(instrument);
    }

    private static void persistInstrument(Instrument instrument) throws MarketNotFoundException {
        if (instrument.getInstrumentType() == InstrumentType.STOCK) {
            stockGateWay.saveStock((Stock) instrument);
        } else if (instrument.getInstrumentType() == InstrumentType.INDEX) {
            indexGateWay.saveIndex((Index) instrument);
        }
    }

    public static void loadInstrumentsForMarket(UUID marketUuid) throws MarketNotFoundException {
        Market market = MarketManager.getMarketByUuid(marketUuid);
        loadStocksForMarket(market);
        loadIndicesForMarket(market);
    }

    private static void loadIndicesForMarket(Market market) {
        Iterable<Index> indices = indexGateWay.getIndicesByMarketUuid(market.getUuid());
        if (indices != null) {
            indices.forEach(market::addInstrument);
        }
    }

    private static void loadStocksForMarket(Market market) {
        Iterable<Stock> stocks = stockGateWay.getStocksByMarketUuid(market.getUuid());
        stocks.forEach(market::addInstrument);
    }

    private static void validateInstrumentCode(Instrument instrument) throws MarketNotFoundException, InstrumentCodeAlreadyOnMarketException {
        Market market = MarketManager.getMarketByUuid(instrument.getMarketUuid());
        if (market.isInstrumentCodeOnMarket(instrument)) {
            throw new InstrumentCodeAlreadyOnMarketException();
        }
    }

    public static void createMarket(String marketName) throws MarketByThisNameAlreadyExistsException {
        Iterable<Market> currentlyPresentMarkets = getAllMarkets();
        for (Market market : currentlyPresentMarkets) {
            if (Objects.equals(market.getName(), marketName)) {
                throw new MarketByThisNameAlreadyExistsException();
            }
        }
        createNewMarketAddToManagerAndPersist(marketName);
    }

    public static Iterable<Instrument> getAllInstrumentsForMarket(UUID marketUUid) throws MarketNotFoundException {
        Market market = MarketManager.getMarketByUuid(marketUUid);
        return market.getAllInstruments();
    }

    private static void createNewMarketAddToManagerAndPersist(String marketName) {
        Market market = new Market(marketName, UUID.randomUUID());
        MarketManager.addMarket(market);
        marketGateWay.saveMarket(market);
    }

    public static void pushRecordToMarket(UUID marketUuid, Record record) throws MarketNotFoundException {
        Market market = MarketManager.getMarketByUuid(marketUuid);
        if (record instanceof HistoricalStockRecord) {
            pushHistoricalRecord(record, marketUuid);
        } else if (record instanceof StockRecord) {
            market.pushRecord(record);
        }
    }

    private static void pushHistoricalRecord(Record record, UUID marketUuid) {
        HistoricalStockRecord historicalStockRecord = (HistoricalStockRecord) record;
        Iterable<HistoricalStockRecord> previous = historicalStockRecordGateWay.getHistoricalStockRecordForPeriod(
                historicalStockRecord.getInstrumentUuid(),
                marketUuid,
                historicalStockRecord.getDate(),
                historicalStockRecord.getDate());
        if (Iterables.size(previous) == 0) {
            historicalStockRecordGateWay.saveHistoricalStockRecord((HistoricalStockRecord) record);
        }
    }

    public static Iterable<HistoricalStockRecord> getHistoricalRecordsForInstrument(UUID instrumentUuid, UUID marketUuid, Date startDate, Date endDate) throws MarketNotFoundException, InstrumentUuidNotFoundException {
        Market market = MarketManager.getMarketByUuid(marketUuid);
        if (!market.isInstrumentUuidOnTheMarket(instrumentUuid)) {
            throw new InstrumentUuidNotFoundException();
        }

        HistoricalStockRecordGateWay historicalStockRecordGateWay = EntityGateWayManager.getHistoricalStockRecordGateWay();
        return historicalStockRecordGateWay.getHistoricalStockRecordForPeriod(instrumentUuid,
                marketUuid,
                startDate,
                endDate);
    }

    public static Iterable<StockRecord> getCurrentRecordsForPeriod(UUID instrumentUuid, UUID marketUuid, Date dateStart, Date dateEnd) {
        return currentStockRecordGateWay.getCurrentRecordsForPeriod(
                instrumentUuid,
                marketUuid,
                dateStart,
                dateEnd);
    }

    public static Record ItegetCurrentRecordForInstrument(UUID instrumentUUID, UUID marketUUID) throws MarketNotFoundException, InstrumentUuidNotFoundException {
        Market market = MarketManager.getMarketByUuid(marketUUID);
        return market.getCurrentRecord(instrumentUUID);
    }

    public static void saveCurrentRecordsToDatabase(UUID marketUUid) throws MarketNotFoundException {
        Market market = MarketManager.getMarketByUuid(marketUUid);
        Iterable<Record> stockRecords = market.getAllCurrentRecords();
        CurrentStockRecordGateWay stockRecordGateWay = EntityGateWayManager.getCurrentStockRecordGateWay();
        for (Record record : stockRecords) {
            stockRecordGateWay.save((StockRecord) record);
        }
    }
}
