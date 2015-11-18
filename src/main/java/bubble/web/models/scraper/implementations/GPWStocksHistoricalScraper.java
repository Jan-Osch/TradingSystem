package bubble.web.models.scraper.implementations;

import bubble.web.models.instrument.Stock;
import bubble.web.models.instrument.manager.StockManager;
import bubble.web.models.record.HistoricalStockRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


public class GPWStocksHistoricalScraper {
    private StockManager stockManager;
    private String baseUrl;

    public GPWStocksHistoricalScraper(String baseUrl, StockManager stockManager) {
        this.baseUrl = baseUrl;
        this.stockManager = stockManager;
    }

    Iterable<HistoricalStockRecord> scrapUrl(String urlToScrap, Date historicalDate) {
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
                    result.add(currentHistoricalStockRecord);
                }
            }
            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private HistoricalStockRecord parseRow(Element row, Date historicalDate) {
        try {
            Elements columns = row.getElementsByTag("td");

            String stockName = columns.get(0).text();
            Stock stock = (Stock) this.stockManager.getInstrumentByCode(this.stockManager.getStockCodeByFullName(stockName));

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
            String changeAsString = columns.get(7).text().replaceAll("[^0-9]", "");
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
            return null;
        }

    }
}
