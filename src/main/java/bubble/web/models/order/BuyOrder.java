package bubble.web.models.order;

import bubble.web.models.account.Account;
import bubble.web.models.index.Index;

import java.util.Date;

public class BuyOrder extends Order {
    public BuyOrder(Account account, Index index, int numberOfShares, Date expirationDate) {
        super(account, index, numberOfShares, expirationDate);
    }
}
