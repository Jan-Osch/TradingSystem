package main.orchestrator;

import markets.entities.market.Market;
import markets.entities.market.MarketManager;
import markets.exceptions.MarketNotFoundException;
import markets.interactors.SaveCurrentRecordsToDatabaseTransaction;
import scraper.implementations.GPWStocksHistoricalScraper;
import scraper.implementations.GPWStocksScraper;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class WarsawOrchestrator {
    private Market warsaw;
    private UUID warsawUuid;
    private static int delay = 15;
    private Timer mainTimer;
    private Timer scrapingTimer;
    private Timer recordSavingTimer;
    private boolean sessionOpen;
    private GPWStocksScraper gpwStocksScraper;
    private GPWStocksHistoricalScraper gpwStocksHistoricalScraper;

    public WarsawOrchestrator(UUID warsawUuid) {
        try {
            this.warsaw = MarketManager.getMarketByUuid(warsawUuid);
        } catch (MarketNotFoundException e) {
            e.printStackTrace();
        }
        this.warsawUuid = warsawUuid;
        this.gpwStocksScraper = new GPWStocksScraper(this.warsawUuid);
        this.gpwStocksHistoricalScraper = new GPWStocksHistoricalScraper(this.warsawUuid);
        this.scrapingTimer = new Timer();
        this.mainTimer = new Timer();
        this.recordSavingTimer = new Timer();
        this.sessionOpen = false;
    }

    private class ScrapingTask extends TimerTask {
        @Override
        public void run() {
            try {
                gpwStocksScraper.scrap();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SaveRecordTask extends TimerTask {
        @Override
        public void run() {
            SaveCurrentRecordsToDatabaseTransaction saveCurrentRecordsToDatabaseTransaction = new SaveCurrentRecordsToDatabaseTransaction(warsawUuid);
            saveCurrentRecordsToDatabaseTransaction.execute();
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
        System.out.println("Warsaw closing session");
        sessionOpen = false;
        scrapingTimer.cancel();
        recordSavingTimer.cancel();
    }

    private void startSession() {
        sessionOpen = true;
        System.out.println("Warsaw opening session");
        gpwStocksHistoricalScraper.scrapIntAmountOfTradingDays(1, 0);
        scrapingTimer.scheduleAtFixedRate(new ScrapingTask(), 0, 10000l);
        recordSavingTimer.scheduleAtFixedRate(new SaveRecordTask(), 10000l, 60000l);
    }

    public void startOrchestratingTheMarket() {
        Date nextTick = getNextSessionStart();
        if (shouldSessionBeOpen()) {
            startSession();
            nextTick = getNextSessionFinsish();
            System.out.println("Warsaw: Session should be open");
        }else {
            adjustDate(nextTick);
            System.out.println("Warsaw: Session will start at: "+ nextTick.toString());
        }
        this.mainTimer.schedule(new TickTask(), nextTick);
    }

    private void adjustDate(Date nextTick) {
        Date currentTime = new Date();
        if(currentTime.compareTo(nextTick) >0){
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
