package com.bubble.scraper.implementations;

import com.bubble.markets.entities.instrument.Instrument;
import com.bubble.markets.entities.instrument.Stock;
import com.bubble.markets.entities.record.StockRecord;
import com.bubble.markets.exceptions.MarketNotFoundException;
import com.bubble.markets.interactors.MarketsInteractor;
import com.bubble.scraper.StockScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.*;

public class GPWStocksScraper extends StockScraper {
    private Document document;
    private final String urlToParse;

    public GPWStocksScraper(UUID marketUuid) {
        super(marketUuid);
        this.urlToParse = "http://www.gpw.pl/ajaxindex.php?action=GPWQuotations&start=showTable&tab=all&lang=PL&full=1";
    }

    public void scrap() throws IOException {
        try {
            this.document = Jsoup.connect(this.urlToParse).post();
            this.document = Jsoup.parse(this.document.text());
            for (StockRecord stockRecord : this.parseDocument()) {

                try {
                    MarketsInteractor.pushRecordToMarket(this.marketUuid, stockRecord);
                } catch (MarketNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IndexOutOfBoundsException | SocketTimeoutException e) {
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
            valueAsString = columns.get(6).text().replaceAll("[^0-9]", "");
        }
        if (Objects.equals(valueAsString, "")) {
            valueAsString = "-1";
        }
        BigDecimal value = new BigDecimal(valueAsString);
        String codeName = columns.get(3).text();
        String stockName = columns.get(2).text().replaceAll("/.*", "").replaceAll("\u00A0", "");
        Stock stockFromMarket = getStockFromMarket(codeName, stockName);
        return new StockRecord(stockFromMarket, value, new Date());
    }

    private Stock getStockFromMarket(String stockCode, String stockName) {
        Iterable<Instrument> allInstruments = this.market.getAllInstruments();
        try {
            return getStockByCode(stockCode, allInstruments);
        } catch (NoSuchElementException ignored) {
        }
        try {
            return getStockByFullName(stockName, allInstruments);
        } catch (NoSuchElementException ignored) {
        }
        return createNewStockOnMarket(stockName, stockCode);
    }
}
