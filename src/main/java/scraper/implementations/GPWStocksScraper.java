package scraper.implementations;

import markets.entities.instrument.Stock;
import markets.entities.manager.InstrumentManager;
import markets.entities.manager.StockManager;
import markets.entities.record.StockRecord;
import scraper.Scraper;
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
        String valueAsString = columns.get(11).text().replaceAll("[^0-9]", "");
        if (Objects.equals(valueAsString, "")) {
            valueAsString = "-1";
        }
        BigDecimal value = new BigDecimal(valueAsString);
        String codeName = columns.get(3).text();
        String stockName = columns.get(2).text().replaceAll("/.*","").replaceAll("\u00A0","");
        if (!this.stockManager.tracksInstrumentWithCode(codeName)) {
            try {
                this.stockManager.createStock(codeName, stockName);
            } catch (InstrumentManager.InstrumentAlreadyTracked instrumentAlreadyTracked) {
                instrumentAlreadyTracked.printStackTrace();
            }
        }
        return new StockRecord((Stock)this.stockManager.getInstrumentByCode(codeName), value);
    }
}
