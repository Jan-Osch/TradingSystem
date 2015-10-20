package models;

import commons.IdHelper;

import java.math.BigDecimal;

/**
 * @author Janusz.
 */
public class Order {
    private int id;
    private Account account;
    private Index index;
    private BigDecimal maximumValue;
    private int numberOfShares;

    public Order(Account account, Index index, BigDecimal maximumValue, int numberOfShares) {
        this.id = IdHelper.getInstance().getNewId();
        this.account = account;
        this.index = index;
        this.maximumValue = maximumValue;
        this.numberOfShares = numberOfShares;
    }
}
