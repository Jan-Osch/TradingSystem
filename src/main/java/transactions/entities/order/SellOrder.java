package account.order;

import account.entities.account.Account;
import markets.entities.instrument.Index;

import java.util.Date;

public class SellOrder extends Order{
    public SellOrder(Account account, Index index, int numberOfShares, Date expirationDate) {
        super(account, index, numberOfShares, expirationDate);
    }
}
