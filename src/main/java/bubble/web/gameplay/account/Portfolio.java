package bubble.web.gameplay.account;

import bubble.web.instruments.instrument.InstrumentType;

import java.util.HashMap;

public class Portfolio {
    protected HashMap<InstrumentType, InstrumentPortfolio> typeInstrumentPortfolioHashMap;

    public Portfolio(HashMap<InstrumentType, InstrumentPortfolio> typeInstrumentPortfolioHashMap) {
        this.typeInstrumentPortfolioHashMap = typeInstrumentPortfolioHashMap;
    }
}
