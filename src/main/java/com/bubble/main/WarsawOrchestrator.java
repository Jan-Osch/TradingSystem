package com.bubble.main;

import com.bubble.markets.entities.market.Market;
import com.bubble.markets.entities.market.MarketManager;
import com.bubble.markets.exceptions.MarketNotFoundException;
import com.bubble.markets.interactors.MarketsInteractor;
import com.bubble.scraper.implementations.GPWStocksHistoricalScraper;
import com.bubble.scraper.implementations.GPWStocksScraper;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class WarsawOrchestrator {
    private Market warsaw;
    private UUID warsawUuid = UUID.fromString("d93e338a-6d0c-4ae7-a730-f84a22eac0cc");
    private static int delay = 15;
    private long periodSavingRecords = 5 * 60000;
    private Timer mainTimer;
    private Timer scrapingTimer;
    private Timer recordSavingTimer;
    private boolean sessionOpen;
    private GPWStocksScraper gpwStocksScraper;
    private GPWStocksHistoricalScraper gpwStocksHistoricalScraper;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(WarsawOrchestrator.class);

    public WarsawOrchestrator() {
        try {
            this.warsaw = MarketManager.getMarketByUuid(this.warsawUuid);
        } catch (MarketNotFoundException e) {
            e.printStackTrace();
        }
        this.gpwStocksScraper = new GPWStocksScraper(this.warsawUuid);
        this.gpwStocksHistoricalScraper = new GPWStocksHistoricalScraper(this.warsawUuid);
        this.scrapingTimer = new Timer();
        this.mainTimer = new Timer();
        this.recordSavingTimer = new Timer();
        this.sessionOpen = false;
        startWarsaw();
    }

    private class ScrapingTask extends TimerTask {
        @Override
        public void run() {
            try {
                System.out.println("Warsaw: scraping current stock records");
                gpwStocksScraper.scrap();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SaveRecordTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("Warsaw: saving current stock records to database");
            try {
                MarketsInteractor.saveCurrentRecordsToDatabase(warsawUuid);
            } catch (MarketNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private class TickTask extends TimerTask {
        @Override
        public void run() {
            Date nextTick;
            if (sessionOpen) {
                finishSession();
                nextTick = getNextSessionFinsish();
            } else {
                startSession();
                nextTick = getNextSessionStart();
            }
            mainTimer.schedule(new TickTask(), nextTick);
        }
    }

    private void finishSession() {
        System.out.println("Warsaw: closing session");
        sessionOpen = false;
        scrapingTimer.cancel();
        recordSavingTimer.cancel();
    }

    private void startSession() {
        sessionOpen = true;
        System.out.println("Warsaw: opening session");
        System.out.println("Warsaw: scraping historical records from last session");
        gpwStocksHistoricalScraper.scrapIntAmountOfTradingDays(1, 0);
        scrapingTimer.scheduleAtFixedRate(new ScrapingTask(), 0, 10000l);
        recordSavingTimer.scheduleAtFixedRate(new SaveRecordTask(), 10000l, this.periodSavingRecords);
    }

    private void startWarsaw() {
        LOG.info("Hello welt");

        try {
            gpwStocksScraper.scrap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Date nextTick = getNextSessionStart();
        if (shouldSessionBeOpen()) {
            System.out.println("Warsaw: Session should be open");
            startSession();
            nextTick = getNextSessionFinsish();
        } else {
            adjustDate(nextTick);
            System.out.println("Warsaw: Session will start at: " + nextTick.toString());

            System.out.println("Warsaw: populating market with current records");
            try {
                gpwStocksScraper.scrap();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.mainTimer.schedule(new TickTask(), nextTick);
    }

    private void adjustDate(Date nextTick) {
        Date currentTime = new Date();
        if (currentTime.compareTo(nextTick) > 0) {
            addDay(nextTick);
        }
    }

    private boolean shouldSessionBeOpen() {
        Date nextSessionFinsish = getNextSessionFinsish();
        Date nextSessionStart = getNextSessionStart();
        Date date = new Date();
        if (date.compareTo(nextSessionStart) > 0) {
            if (date.compareTo(nextSessionFinsish) < 0) {
                return true;
            }
        }
        return false;
    }

    private static Date getNextSessionStart() {
        Date date = getNextWorkingDay(new Date());
        date.setHours(8);
        date.setMinutes(30 + delay);
        date.setSeconds(0);
        return date;
    }

    private static Date getNextSessionFinsish() {
        Date date = getNextWorkingDay(new Date());
        date.setHours(17);
        date.setMinutes(5 + delay);
        date.setSeconds(0);
        return date;
    }

    private static Date getNextWorkingDay(Date date) {
        while (!isWorkingDay(date)) {
            addDay(date);
        }
        return date;
    }

    private static boolean isWorkingDay(Date date) {
        return (date.getDay() > 0 && date.getDay() < 6);
    }

    private static void addDay(Date date) {
        date.setTime(date.getTime() + 24 * 60 * 60 * 1000);
    }

}
