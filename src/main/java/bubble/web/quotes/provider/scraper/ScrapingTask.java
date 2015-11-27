package bubble.web.quotes.provider.scraper;

import java.io.IOException;
import java.util.TimerTask;

public class ScrapingTask extends TimerTask {

    public ScrapingTask(Scraper scraper) {
        this.scraper = scraper;
    }

    private final Scraper scraper;

    @Override
    public void run() {
        try {
            this.scraper.scrap();
        } catch (IOException e) {
            //todo
        }
    }
}
