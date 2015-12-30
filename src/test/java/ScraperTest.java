import markets.entities.market.Market;
import scraper.implementations.GPWStocksScraper;

import java.io.IOException;
import java.util.Date;

public class ScraperTest {

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date.toString());

        Market warsaw = InitializeApplication.initializeMarket();
        GPWStocksScraper gpwStocksScraper = new GPWStocksScraper(warsaw.getUuid());

        try {
            gpwStocksScraper.scrap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

