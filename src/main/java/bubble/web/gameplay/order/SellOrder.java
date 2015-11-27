package bubble.web.gameplay.order;

import bubble.web.gameplay.account.Account;
import bubble.web.instruments.instrument.Index;

import java.util.Date;

public class SellOrder extends Order{
    public SellOrder(Account account, Index index, int numberOfShares, Date expirationDate) {
        super(account, index, numberOfShares, expirationDate);
    }
}
