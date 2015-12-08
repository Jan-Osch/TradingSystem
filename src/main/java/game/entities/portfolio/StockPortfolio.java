package game.entities.portfolio;

import markets.entities.instrument.InstrumentType;
import markets.entities.instrument.Stock;

public class StockPortfolio extends InstrumentPortfolio {

    public StockPortfolio(InstrumentType instrumentType, Portfolio portfolio) {
        super(instrumentType, portfolio);
    }

    public void addToPortfolio(Stock stock, int numberOfShares){
        //todo
    }
}
