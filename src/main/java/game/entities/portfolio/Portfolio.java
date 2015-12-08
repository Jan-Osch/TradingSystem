package game.entities.portfolio;

import markets.entities.instrument.InstrumentType;

import java.util.HashMap;

public class Portfolio {
    protected HashMap<InstrumentType, InstrumentPortfolio> typeInstrumentPortfolioHashMap;

    public Portfolio() {
        this.typeInstrumentPortfolioHashMap = new HashMap<InstrumentType, InstrumentPortfolio>();
    }

    public void addInstrumentPortfolio(InstrumentPortfolio instrumentPortfolio) throws InstrumentPortfolioAlreadyExists {
        if (hasInstrumentPortfolioOfGivenType(instrumentPortfolio)) {
            throw new InstrumentPortfolioAlreadyExists();
        }
    }

    private boolean hasInstrumentPortfolioOfGivenType(InstrumentPortfolio instrumentPortfolio) {
        return this.typeInstrumentPortfolioHashMap.containsKey(instrumentPortfolio.getInstrumentType());
    }

    class InstrumentPortfolioAlreadyExists extends Throwable {
    }
}
