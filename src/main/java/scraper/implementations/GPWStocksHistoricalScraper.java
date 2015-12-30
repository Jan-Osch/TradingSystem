package scraper.implementations;

import markets.entities.instrument.Instrument;
import markets.entities.instrument.Stock;
import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.entities.record.HistoricalStockRecord;
import markets.exceptions.InstrumentCodeAlreadyOnMarketException;
import markets.exceptions.MarketNotFoundException;
import markets.interactors.AddInstrumentTransaction;
import markets.interactors.PushRecordTransaction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


public class GPWStocksHistoricalScraper {
    private Market market;
    private UUID marketUuid;
    private String baseUrl;
    private static String dateFormat = "YYYY-MM-dd";
    private static SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

    public GPWStocksHistoricalScraper(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public GPWStocksHistoricalScraper(UUID marketUuid) {
        this.baseUrl = "http://www.gpw.pl/notowania_archiwalne_full?type=10&date=";
        this.marketUuid = marketUuid;
        try {
            this.market = MarketManager.getMarketByUuid(marketUuid);
        } catch (MarketNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void scrapIntAmountOfTradingDays(int i, int skip) {
        Date historicalDate = new Date();

        historicalDate.setTime(historicalDate.getTime() - skip * 24 * 60 * 60 * 1000);
        String currentUrl;
        while (i > 0) {
            historicalDate.setTime(historicalDate.getTime() - 24 * 60 * 60 * 1000);
            currentUrl = baseUrl + formatter.format(historicalDate);
            if (scrapUrl(currentUrl, historicalDate)) {
                i--;
                System.out.println("Sucessfully scraped 1 day of historical data");
            }
        }
    }

    private boolean scrapUrl(String urlToScrap, Date historicalDate) {
        boolean flag = false;
        try {
            Document currentDocument = Jsoup.connect(urlToScrap).get();
            assert (currentDocument.getElementsByClass("tab03").size() == 1);
            Element table = currentDocument.getElementsByClass("tab03").get(0);
            Elements rows = table.getElementsByTag("tr");
            ArrayList<HistoricalStockRecord> result = new ArrayList<>();
            HistoricalStockRecord currentHistoricalStockRecord;
            for (Element row : rows) {
                currentHistoricalStockRecord = parseRow(row, historicalDate);
                if (currentHistoricalStockRecord != null) {

                    PushRecordTransaction pushRecordTransaction = new PushRecordTransaction(this.marketUuid, currentHistoricalStockRecord);
                    try {
                        pushRecordTransaction.execute();
                    } catch (MarketNotFoundException e) {
                        e.printStackTrace();
                    }


                    flag = true;
                }
            }

        } catch (IOException e) {
//            e.printStackTrace();
            return false;
        }catch (IndexOutOfBoundsException e){
            return false;
        }
        return flag;
    }

    private HistoricalStockRecord parseRow(Element row, Date historicalDate) {
        try {
            Elements columns = row.getElementsByTag("td");

            Stock stock = getStockFromMarket(columns);

            String openingAsString = columns.get(3).text().replaceAll("[^0-9]", "");
            if (Objects.equals(openingAsString, "")) {
                openingAsString = "-1";
            }
            String minimumAsString = columns.get(5).text().replaceAll("[^0-9]", "");
            if (Objects.equals(minimumAsString, "")) {
                minimumAsString = "-1";
            }
            String valueAsString = columns.get(6).text().replaceAll("[^0-9]", "");
            if (Objects.equals(valueAsString, "")) {
                valueAsString = "-1";
            }
            String changeAsString = columns.get(7).text().replace(",", ".");
            if (Objects.equals(changeAsString, "")) {
                changeAsString = "-1";
            }
            String volumeAsString = columns.get(8).text().replaceAll("[^0-9]", "");
            if (Objects.equals(volumeAsString, "")) {
                volumeAsString = "-1";
            }
            String transactionAsString = columns.get(9).text().replaceAll("[^0-9]", "");
            if (Objects.equals(transactionAsString, "")) {
                transactionAsString = "-1";
            }

            BigDecimal value = new BigDecimal(valueAsString);
            BigDecimal volume = new BigDecimal(volumeAsString);
            BigDecimal opening = new BigDecimal(openingAsString);
            BigDecimal minimum = new BigDecimal(minimumAsString);
            Float change = new Float(changeAsString);
            int transaction = Integer.valueOf(transactionAsString);

            return new HistoricalStockRecord(stock, historicalDate, volume, opening, value, minimum, change, transaction);

        } catch (Throwable e) {
//            e.printStackTrace();
            return null;
        }
    }

    private Stock getStockFromMarket(Elements columns) {
        String stockName = columns.get(0).text();
        String stockCode;
        Stock stock = getStockByFullName(stockName);
        if(stock == null){
            System.out.printf("Stock not found by fullName: %s.\n", stockName);
            String stockISINCode = columns.get(1).text();
            stockCode = getMissingCode(stockISINCode);
            stock = new Stock(UUID.randomUUID(), this.marketUuid, stockCode, stockName);

            AddInstrumentTransaction addInstrumentTransaction = new AddInstrumentTransaction(stock);
            try {
                addInstrumentTransaction.execute();
            } catch (MarketNotFoundException e) {
//                e.printStackTrace();
            } catch (InstrumentCodeAlreadyOnMarketException e) {
//                e.printStackTrace();
            }
            return stock;
        }
        return stock;
    }

    private Stock getStockByFullName(String stockName) {
        Iterable<Instrument> allInstruments = this.market.getAllInstruments();
        for (Instrument instrument : allInstruments) {
            if (Objects.equals(instrument.getFullName(), stockName)) {
                return (Stock) instrument;
            }
        }
        return null;
    }

    private String getMissingCode(String stockISINCode) {
        try {
            Document toParse = Jsoup.connect("http://www.gpw.pl/karta_spolki/" + stockISINCode + "/").get();
            String title = toParse.title();
            return title.substring(title.indexOf("|") + 2, title.lastIndexOf("|") - 1);
        } catch (IOException e) {
//            e.printStackTrace();
            return null;
        }
    }
}
