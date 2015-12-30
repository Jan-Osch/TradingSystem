//package game.entities.portfolio;
//
//import markets.entities.instrument.InstrumentType;
//import markets.entities.market.Market;
//
//public class PortfolioFactory {
//
//    public static Portfolio createPortfolio(Market market) {
//        Portfolio portfolio = new Portfolio();
//        for (InstrumentType inType : market.()) {
//            try {
//                portfolio.addInstrumentPortfolio(new InstrumentPortfolio(inType, portfolio));
//            } catch (Portfolio.InstrumentPortfolioAlreadyExists instrumentPortfolioAlreadyExists) {
//                instrumentPortfolioAlreadyExists.printStackTrace();
//            }
//        }
//        return portfolio;
//    }
//}
