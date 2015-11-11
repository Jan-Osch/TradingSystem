package bubble.web.models.scraper.implementations;

import bubble.web.models.instrument.Stock;
import bubble.web.models.instrument.manager.StockManager;
import bubble.web.models.record.StockRecord;
import bubble.web.models.scraper.Scraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

public class MyBankScraper implements Scraper {
    private final StockManager stockManager;
    private Document document;
    private final String urlToParse;
    private Map<String, Stock> fullNameToStockMap;

    public MyBankScraper(StockManager stockManager, String urlToParse) {
        this.stockManager = stockManager;
        this.urlToParse = urlToParse;
    }

    public void scrap() throws IOException {
        this.document = Jsoup.connect(this.urlToParse).get();
        for (StockRecord stockRecord : this.parseDocument()) {
            this.stockManager.pushRecord(stockRecord);
        }

    }

    private Iterable<StockRecord> parseDocument() {
        Elements tableWithRecords = this.document.getElementsByClass("g_tab");
        if (tableWithRecords.size() != 1) {
            throw new InputMismatchException();
        }
        Elements rows = tableWithRecords.first().getElementsByTag("tr");
        ArrayList<StockRecord> result = new ArrayList<StockRecord>();
        StockRecord current;

        int index = 0;
        for (Element row : rows) {
            if (index > 1) {
                current = this.parseRecordFromRow(row);
                if (current != null) {
                    result.add(current);
                }
            }
            index++;
        }
        return result;
    }

    private StockRecord parseRecordFromRow(Element root) {
        Elements columns = root.getElementsByTag("td");
        String stringValue = columns.get(2).text().replace(".", "").replace(" ", "");
        if (stringValue.contains("-")) {
            return null;
        }
        String stringVolume = columns.get(6).text().replace(" ", "");
        if (stringVolume.contains("-")) {
            return null;
        }
        BigDecimal value = new BigDecimal(stringValue);
        BigDecimal volume = new BigDecimal(stringVolume);
        String stockName = columns.get(0).text();
        if (this.stockManager.getStockCodeByFullName(stockName) == null) {
            return null;
        }
        String codeName = this.stockManager.getStockCodeByFullName(stockName);
        return new StockRecord(this.stockManager.getInstrumentByCode(codeName), value, volume);
    }
}
