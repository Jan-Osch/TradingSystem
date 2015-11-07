package models;

import java.util.Date;

public class SellOrder extends Order{
    protected SellOrder(Account account, Index index, int numberOfShares, Date expirationDate) {
        super(account, index, numberOfShares, expirationDate);
    }
}
