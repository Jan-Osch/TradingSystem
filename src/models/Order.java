package models;

import commons.IdHelper;

import java.math.BigDecimal;

public abstract class Order {
    private final int id;
    private final Account account;
    private final Index index;
    private final int numberOfShares;

    protected Order(Account account, Index index, int numberOfShares) {
        this.id = IdHelper.getInstance().getNewId();
        this.account = account;
        this.index = index;
        this.numberOfShares = numberOfShares;
    }

    public Account getAccount() {
        return account;
    }

    public Index getIndex() {
        return index;
    }

    public int getNumberOfShares() {
        return numberOfShares;
    }
}
