package bubble.web.gameplay.order;

import bubble.web.gameplay.account.Account;
import bubble.web.instruments.instrument.Index;

import java.util.Date;

public class BuyOrder extends Order {
    public BuyOrder(Account account, Index index, int numberOfShares, Date expirationDate) {
        super(account, index, numberOfShares, expirationDate);
    }
}
