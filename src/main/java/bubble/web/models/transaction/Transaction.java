package bubble.web.models.transaction;

import bubble.web.commons.IdHelper;
import bubble.web.models.index.Index;
import bubble.web.models.order.BuyOrder;
import bubble.web.models.order.SellOrder;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private final int id;
    private final BuyOrder buyOrder;
    private final SellOrder sellOrder;
    private final Date dateOfRealisation;
    private final Index index;
    private final BigDecimal course;
    private final BigDecimal totalValue;
    private final int numberOfShares;

    public Transaction(BuyOrder buyOrder, SellOrder sellOrder, Index index, BigDecimal course, BigDecimal totalValue, int numberOfShares) {
        this.id = IdHelper.getInstance().getNewId();
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.dateOfRealisation = new Date();
        this.index = index;
        this.course = course;
        this.totalValue = totalValue;
        this.numberOfShares = numberOfShares;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public int getNumberOfShares() {
        return numberOfShares;
    }

    public Index getIndex() {
        return index;
    }
}
