package models;

import commons.IdHelper;

public class SellOrder {
    private final int id;
    private final Account account;
    private final Index index;
    private final int numberOfShares;

    public SellOrder(Account account, Index index, int numberOfShares) {
        this.id = IdHelper.getInstance().getNewId();
        this.account = account;
        this.index = index;
        this.numberOfShares = numberOfShares;
    }
}
