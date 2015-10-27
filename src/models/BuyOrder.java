package models;

import commons.IdHelper;

import java.util.Date;

public class BuyOrder extends Order {
    public BuyOrder(Account account, Index index, int numberOfShares, Date expirationDate) {
        super(account, index, numberOfShares, expirationDate);
    }
}
