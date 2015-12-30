import database.PostgresHistoricalStockRecordDao;
import database.PostgresMarketDao;
import database.PostgresStockDao;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.exceptions.MarketNotFoundException;
import markets.interactors.LoadInstrumentsForMarketTransaction;
import markets.interactors.LoadMarketsTransaction;
import persistance.EntityGateWayManager;
import scraper.implementations.GPWStocksHistoricalScraper;

import java.util.Iterator;

public class HistoricalScraperTest {

    public static void main(String[] args) {

        Market warsaw = InitializeApplication.initializeMarket();

        GPWStocksHistoricalScraper gpwStocksHistoricalScraper = new GPWStocksHistoricalScraper(warsaw.getUuid());

        gpwStocksHistoricalScraper.scrapIntAmountOfTradingDays(50,0);

    }
}
