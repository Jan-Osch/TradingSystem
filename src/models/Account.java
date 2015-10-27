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
    private int id;
    private User owner;
    private BigDecimal currentState;
    private List<Transaction> historyOfTransactions;
    private Map porfolio;

    public Account(User owner) {
        this.id = IdHelper.getInstance().getNewId();
        this.owner = owner;
        this.currentState = BigDecimal.valueOf(0);
        this.historyOfTransactions = new ArrayList<>();
        this.porfolio = new TreeMap<>();
    }

    private void addToAccount(BigDecimal valueToAdd) {
        this.currentState = this.currentState.add(valueToAdd);
    }

    private void subtractFromAccout(BigDecimal valueToSubtract){
        if(this.currentState.compareTo(valueToSubtract) == -1 ){
            throw new IllegalArgumentException("Attempting to subtract from account more than current state.");
        }
        this.currentState = this.currentState.subtract(valueToSubtract);
    }

    private void addBuyTransaction(Transaction transaction){
        this.subtractFromAccout(transaction.getAmout());
        this.porfolio.put(transaction)
        this.historyOfTransactions.add(transaction);
    }

    private void addSellTransaction(Transaction transaction){
        this.subtractFromAccout(transaction.getAmout());
        this.historyOfTransactions.add(transaction);
    }
}
