package bubble.web.models.scraper;


import bubble.web.models.instrument.InstrumentType;
import bubble.web.models.instrument.manager.StockManager;
import bubble.web.models.instrument.quotes.cache.InstrumentQuotesCache;
import bubble.web.models.instrument.quotes.container.InstrumentQuotesHistoricalContainer;
import bubble.web.models.instrument.quotes.container.InstrumentQuotesIntervalContainer;
import bubble.web.models.market.Market;
import bubble.web.models.scraper.implementations.GPWStocksHistoricalScraper;
import bubble.web.models.scraper.implementations.GPWStocksScraper;

class Simulation {
    public static void main(String[] args) {

        //StockManager
        InstrumentQuotesIntervalContainer intervalContainerOneMinute = new InstrumentQuotesIntervalContainer(3, "GPW_STOCKS_CURRENT_1_MINUTE");
        InstrumentQuotesHistoricalContainer gpwHistoricalQuotesContainer = new InstrumentQuotesHistoricalContainer("GPW_STOCKS_HISTORICAL");
        StockManager warsawStockManager = new StockManager(new InstrumentQuotesCache(), intervalContainerOneMinute, gpwHistoricalQuotesContainer, 5000, 10000);

        //Market
        Market warsaw = new Market("Warsaw GPW");
        warsaw.addInstrumentToMarket(InstrumentType.STOCK, warsawStockManager);

        //Scraper
        GPWStocksScraper gpwStocksScraper = new GPWStocksScraper(warsawStockManager);
        ScrapingTask gpwStocksScrapingTask = new ScrapingTask(gpwStocksScraper);
        ScrapingManager scrapingManager = ScrapingManager.getInstance();
        scrapingManager.startScraping(gpwStocksScrapingTask, 1000);


        //Historical data
        GPWStocksHistoricalScraper gpwStocksHistoricalScraper = new GPWStocksHistoricalScraper(warsawStockManager);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gpwStocksHistoricalScraper.scrapIntAmountOfTradingDays(30);
    }
}
