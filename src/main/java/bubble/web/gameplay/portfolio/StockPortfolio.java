package bubble.web.gameplay.portfolio;

import bubble.web.instruments.instrument.InstrumentType;
import bubble.web.instruments.instrument.Stock;

public class StockPortfolio extends InstrumentPortfolio {

    public StockPortfolio(InstrumentType instrumentType, Portfolio portfolio) {
        super(instrumentType, portfolio);
    }

    public void addToPortfolio(Stock stock, int numberOfShares){
        //todo
    }
}
