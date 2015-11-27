package bubble.web.gameplay.account;

import bubble.web.instruments.instrument.Index;
import bubble.web.instruments.instrument.InstrumentType;

public class InstrumentPortfolio {
    private InstrumentType instrumentType;

    public InstrumentPortfolio(InstrumentType instrumentType) {
        this.instrumentType = instrumentType;
    }
//
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
