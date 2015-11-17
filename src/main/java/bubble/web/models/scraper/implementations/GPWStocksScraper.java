package bubble.web.models.scraper.implementations;

import bubble.web.models.instrument.manager.StockManager;
import bubble.web.models.record.StockRecord;
import bubble.web.models.scraper.Scraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Objects;

public class GPWStocksScraper implements Scraper {
    private final StockManager stockManager;
    private Document document;
    private final String urlToParse;

    public GPWStocksScraper(StockManager stockManager, String urlToParse) {
        this.stockManager = stockManager;
        this.urlToParse = urlToParse;
    }

    public GPWStocksScraper(StockManager stockManager) {
        this.stockManager = stockManager;
        this.urlToParse = "http://www.gpw.pl/ajaxindex.php?action=GPWQuotations&start=showTable&tab=all&lang=PL&full=1";
    }

    public void scrap() throws IOException {
        try {
            this.document = Jsoup.connect(this.urlToParse).post();
            this.document = Jsoup.parse(this.document.text());
            for (StockRecord stockRecord : this.parseDocument()) {
                this.stockManager.pushRecord(stockRecord);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IOException();
        } catch (SocketTimeoutException e) {
            throw new IOException();
        }
    }

    private Iterable<StockRecord> parseDocument() {
        Elements rows = this.document.getElementsByTag("tr");
        ArrayList<StockRecord> result = new ArrayList<StockRecord>();
        StockRecord current;
        for (Element row : rows) {
            try {
                current = this.parseRecordFromRow(row);
            } catch (IndexOutOfBoundsException e) {
                current = null;
            }
            if (current != null) result.add(current);
        }
        return result;
    }

    private StockRecord parseRecordFromRow(Element root) {
        Elements columns = root.getElementsByTag("td");
        String volumeAsString = columns.get(22).text().replaceAll("[^0-9]", "");
        if (Objects.equals(volumeAsString, "")) {
            volumeAsString = "-1";
        }
        String valueAsString = columns.get(6).text().replaceAll("[^0-9]", "");
        if (Objects.equals(valueAsString, "")) {
            valueAsString = "-1";
        }
        BigDecimal volume = new BigDecimal(volumeAsString);
        BigDecimal value = new BigDecimal(valueAsString);
        String stockName = columns.get(2).text();
        String codeName = columns.get(3).text();
        if (!this.stockManager.tracksInstrumentWithCode(codeName)) {
            this.stockManager.createStock(codeName, stockName);
        }
        return new StockRecord(this.stockManager.getInstrumentByCode(codeName), value, volume);
    }
}
