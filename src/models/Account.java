package models;

import commons.IdHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Janusz.
 */
public class Account {
    private final int id;
    private final User owner;
    private BigDecimal currentState;
    private List<Transaction> historyOfTransactions;
    private Map<Index, Integer> portfolio;

    public Account(User owner) {
        this.id = IdHelper.getInstance().getNewId();
        this.owner = owner;
        this.currentState = BigDecimal.valueOf(0);
        this.historyOfTransactions = new ArrayList<>();
        this.portfolio = new TreeMap<>();
    }

    public BigDecimal getCurrentState() {
        return currentState;
    }

    public void addToAccount(BigDecimal valueToAdd) {
        this.currentState = this.currentState.add(valueToAdd);
    }

    public void addToAccount(int value) {
        this.currentState = this.currentState.add(BigDecimal.valueOf(value));
    }

    void subtractFromAccount(BigDecimal valueToSubtract) {
        if (this.currentState.compareTo(valueToSubtract) == -1) {
            throw new IllegalArgumentException("Attempting to subtract from account more than current state.");
        }
        this.currentState = this.currentState.subtract(valueToSubtract);
    }

    public void addStockToPortfolio(Index index, int number) {
        Integer newNumberOfShares = number;
        if (this.portfolio.containsKey(index)) {
            Integer currentNumberOfShares = this.portfolio.get(index);
            newNumberOfShares += currentNumberOfShares;
        }
        this.portfolio.put(index, newNumberOfShares);
    }

    private void removeStockFromPortfolio(Index index, int number) {
        if (!this.portfolio.containsKey(index)) {
            throw new IllegalArgumentException("This portfolio does not contain any shares of given index");
        }
        if (this.portfolio.get(index) < number) {
            throw new IllegalArgumentException("This portfolio does not contain enough shares of given index");
        }
        Integer newNumberOfShares = this.portfolio.get(index) - number;
        if (newNumberOfShares == 0) {
            this.portfolio.remove(index);
        } else {
            this.portfolio.put(index, newNumberOfShares);
        }
    }

    public void addBuyTransaction(Transaction transaction) {
        this.subtractFromAccount(transaction.getTotalValue());
        this.addStockToPortfolio(transaction.getIndex(), transaction.getNumberOfShares());
        this.historyOfTransactions.add(transaction);
    }

    public void addSellTransaction(Transaction transaction) {
        this.addToAccount(transaction.getTotalValue());
        this.removeStockFromPortfolio(transaction.getIndex(), transaction.getNumberOfShares());
        this.historyOfTransactions.add(transaction);
    }

    public int getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public int getNumberOfSharesInPortfolio(Index index) {
        if (!portfolio.containsKey(index)) {
            return 0;
        }
        return portfolio.get(index);
    }

    public void printPortfolio() {
        for (Index i : this.portfolio.keySet()) {
            System.out.println("Index: " + i.getName()
                    + " number of shares: " + this.getNumberOfSharesInPortfolio(i)
                    + " current value " + i.getCurrentValue().multiply(BigDecimal.valueOf(this.getNumberOfSharesInPortfolio(i))));
        }
    }

    public BigDecimal getTotalPortfolioValue(){
        BigDecimal result = new BigDecimal(0);
        for (Index i : this.portfolio.keySet()) {
            result = result.add(i.getCurrentValue().multiply(BigDecimal.valueOf(this.getNumberOfSharesInPortfolio(i))));
        }
        return result;
    }
}
