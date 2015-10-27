package models;

import commons.IdHelper;

public class BuyOrder extends Order {
    public BuyOrder(Account account, Index index, int numberOfShares) {
        super(account, index, numberOfShares);
    }
}
