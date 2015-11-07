package bubble.web.models.stocks.quotes.provider;


import bubble.web.models.stocks.StockManager;

import java.io.IOException;

class MyBankScraperTest {
    public static void main(String[] args) {
        StockManager stockManager = StockManager.getInstance();
        MyBankScraper scraper = new MyBankScraper(stockManager, "http://akcje.mybank.pl/");
        try {
            while (true) {
                scraper.scrap();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
