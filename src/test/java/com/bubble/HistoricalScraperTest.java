import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.exceptions.MarketNotFoundException;
import persistance.EntityGateWayManager;
import scraper.implementations.GPWStocksHistoricalScraper;

public class HistoricalScraperTest {

    public static void main(String[] args) {

        Market warsaw = InitializeApplication.initializeMarket();

        GPWStocksHistoricalScraper gpwStocksHistoricalScraper = new GPWStocksHistoricalScraper(warsaw.getUuid());

        gpwStocksHistoricalScraper.scrapIntAmountOfTradingDays(100,21);

    }
}
