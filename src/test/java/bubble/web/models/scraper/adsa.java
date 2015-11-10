package bubble.web.models.scraper;


import bubble.web.models.instrument.InstrumentType;
import bubble.web.models.instrument.manager.StockManager;
import bubble.web.models.instrument.quotes.cache.InstrumentQuotesCache;
import bubble.web.models.instrument.quotes.interval.container.InstrumentQuotesIntervalContainer;
import bubble.web.models.market.Market;

class MyBankScraperTest {
    public static void main(String[] args) {
        Market warsaw = new Market("Warsaw GPW");
        StockManager warsawStockManager = new StockManager(new InstrumentQuotesCache(), new InstrumentQuotesIntervalContainer());
        warsaw.addInstrumentToMarket(InstrumentType.STOCK, warsawStockManager);


        MyBankScraper scraper = new MyBankScraper(warsawStockManager, "http://akcje.mybank.pl/");
        ScrapingTask task = new ScrapingTask(scraper);
        ScrapingManager scrapingManager = ScrapingManager.getInstance();
        scrapingManager.startScraping(task, 5000);
    }
}
