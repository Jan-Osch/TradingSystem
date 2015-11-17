package bubble.web.models.scraper;


import bubble.web.models.instrument.InstrumentType;
import bubble.web.models.instrument.manager.StockManager;
import bubble.web.models.instrument.quotes.cache.InstrumentQuotesCache;
import bubble.web.models.instrument.quotes.interval.container.InstrumentQuotesIntervalContainer;
import bubble.web.models.market.Market;
import bubble.web.models.scraper.implementations.GPWStocksScraper;
import bubble.web.models.scraper.implementations.MyBankScraper;

class MyBankScraperTest {
    public static void main(String[] args) {
        Market warsaw = new Market("Warsaw GPW");

        InstrumentQuotesIntervalContainer tenMinutes = new InstrumentQuotesIntervalContainer(5, "GPW_STOCKS_10_MINUTES");
        InstrumentQuotesIntervalContainer twoMinutes = new InstrumentQuotesIntervalContainer(4, tenMinutes, "GPW_STOCKS_2_MINUTES");
        InstrumentQuotesIntervalContainer halfMinute = new InstrumentQuotesIntervalContainer(3, twoMinutes, "GPW_STOCKS_30_SECONDS");
        StockManager warsawStockManager = new StockManager(new InstrumentQuotesCache(), halfMinute, 5000, 10000);
        warsaw.addInstrumentToMarket(InstrumentType.STOCK, warsawStockManager);


        GPWStocksScraper gpwStocksScraper = new GPWStocksScraper(warsawStockManager);

        ScrapingTask gpwStocksScrapingTask = new ScrapingTask(gpwStocksScraper);
        ScrapingManager scrapingManager = ScrapingManager.getInstance();
        scrapingManager.startScraping(gpwStocksScrapingTask, 2000);
    }
}
