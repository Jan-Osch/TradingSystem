package bubble.web.models.stocks.quotes.provider;

import java.io.IOException;

public interface Scraper {
    void scrap() throws IOException;
}
