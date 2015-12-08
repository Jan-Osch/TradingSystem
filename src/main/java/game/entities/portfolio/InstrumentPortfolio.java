package game.entities.portfolio;

import markets.entities.instrument.InstrumentType;

public class InstrumentPortfolio {
    private InstrumentType instrumentType;
    private Portfolio portfolio;

    public InstrumentPortfolio(InstrumentType instrumentType, Portfolio portfolio) {
        this.instrumentType = instrumentType;
        this.portfolio = portfolio;
    }

    public InstrumentType getInstrumentType() {
        return instrumentType;
    }
//    public void addStockToPortfolio(Index index, int number) {
//        Integer newNumberOfShares = number;
//        if (this.portfolio.containsKey(index)) {
//            Integer currentNumberOfShares = this.portfolio.get(index);
//            newNumberOfShares += currentNumberOfShares;
//        }
//        this.portfolio.put(index, newNumberOfShares);
//    }
//
//    private void removeStockFromPortfolio(Index index, int number) {
//        if (!this.portfolio.containsKey(index)) {
//            throw new IllegalArgumentException("This portfolio does not contain any shares of given index");
//        }
//        if (this.portfolio.get(index) < number) {
//            throw new IllegalArgumentException("This portfolio does not contain enough shares of given index");
//        }
//        Integer newNumberOfShares = this.portfolio.get(index) - number;
//        if (newNumberOfShares == 0) {
//            this.portfolio.remove(index);
//        } else {
//            this.portfolio.put(index, newNumberOfShares);
//        }
//    }
}
