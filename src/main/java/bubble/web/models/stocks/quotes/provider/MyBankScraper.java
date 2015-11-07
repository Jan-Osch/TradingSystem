package bubble.web.models.stocks.quotes.provider;

import bubble.web.models.stocks.Record;
import bubble.web.models.stocks.StockManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

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
        for (Record record : this.parseDocument()) {
            this.stockManager.pushRecord(record);
        }

    }

    private Iterable<Record> parseDocument() {
        Elements tableWithRecords = this.document.getElementsByClass("g_tab");
        if (tableWithRecords.size() != 1) {
            throw new InputMismatchException();
        }
        Elements rows = tableWithRecords.first().getElementsByTag("tr");
        ArrayList<Record> result = new ArrayList<Record>();
        Record current;
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

    private Record parseRecordFromRow(Element root) {
        Elements columns = root.getElementsByTag("td");
        String indexName = columns.get(0).text();
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
        return new Record(value, indexName, volume);
    }
}
