package bubble.web.gameplay.portfolio;

import bubble.web.instruments.instrument.InstrumentType;
import bubble.web.instruments.market.Market;

public class PortfolioFactory {

    public static Portfolio createPortfolio(Market market) {
        Portfolio portfolio = new Portfolio();
        for (InstrumentType inType : market.getTrackedInstruments()) {
            try {
                portfolio.addInstrumentPortfolio(new InstrumentPortfolio(inType, portfolio));
            } catch (Portfolio.InstrumentPortfolioAlreadyExists instrumentPortfolioAlreadyExists) {
                instrumentPortfolioAlreadyExists.printStackTrace();
            }
        }
        return portfolio;
    }
}
