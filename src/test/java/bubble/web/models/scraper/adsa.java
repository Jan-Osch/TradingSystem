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

        InstrumentQuotesIntervalContainer tenMinutes = new InstrumentQuotesIntervalContainer(5, "Ten minutes");
        InstrumentQuotesIntervalContainer twoMinutes = new InstrumentQuotesIntervalContainer(4, tenMinutes, "Two minutes");
        InstrumentQuotesIntervalContainer halfMinute = new InstrumentQuotesIntervalContainer(3, twoMinutes, "30 seconds");
        StockManager warsawStockManager = new StockManager(new InstrumentQuotesCache(), halfMinute, 10000);
        warsaw.addInstrumentToMarket(InstrumentType.STOCK, warsawStockManager);


//        MyBankScraper scraper = new MyBankScraper(warsawStockManager, "http://akcje.mybank.pl/");
        GPWStocksScraper gpwStocksScraper = new GPWStocksScraper(warsawStockManager);

//        ScrapingTask myBankScrapingTask = new ScrapingTask(scraper);
        ScrapingTask gpwStocksScrapingTask = new ScrapingTask(gpwStocksScraper);
        ScrapingManager scrapingManager = ScrapingManager.getInstance();
//        scrapingManager.startScraping(myBankScrapingTask, 5000);
        scrapingManager.startScraping(gpwStocksScrapingTask, 2000);
    }
}
