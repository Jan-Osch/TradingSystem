package bubble.web.models.scraper;

import java.util.*;

public class ScrapingManager {

    private static ScrapingManager instance = null;

    public static ScrapingManager getInstance() {
        if (instance == null) {
            instance = new ScrapingManager();
        }
        return instance;
    }

    private ScrapingManager() {
    }

    public void startScraping(TimerTask timerTask, long intervalInSeconds) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, intervalInSeconds);
    }
}
