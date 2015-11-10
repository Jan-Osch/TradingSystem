package bubble.web.models.scraper;

import bubble.web.models.instrument.manager.StockManager;
import bubble.web.models.record.StockRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class MyBankScraper implements Scraper {
    private final StockManager stockManager;
    private Document document;
    private final String urlToParse;

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
        String indexName = columns.get(0).text();
        if (!this.stockManager.tracksInstrumentWithCode(indexName)) {
            this.stockManager.createInstrumentByCode(indexName);
        }
        return new StockRecord(this.stockManager.getInstrumentByCode(indexName), value, volume);
    }
}
