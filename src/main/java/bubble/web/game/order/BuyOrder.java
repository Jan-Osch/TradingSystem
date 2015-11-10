package bubble.web.game.order;

import bubble.web.game.account.Account;
import bubble.web.models.instrument.Index;

import java.util.Date;

public class BuyOrder extends Order {
    public BuyOrder(Account account, Index index, int numberOfShares, Date expirationDate) {
        super(account, index, numberOfShares, expirationDate);
    }
}
