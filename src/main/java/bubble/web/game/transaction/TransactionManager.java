package bubble.web.game.transaction;


import bubble.web.game.order.BuyOrder;
import bubble.web.game.order.SellOrder;

import java.math.BigDecimal;

public class TransactionManager {
    private static TransactionManager instance = null;

    private TransactionManager() {
    }

    public static TransactionManager getInstance() {
        if (instance == null) {
            instance = new TransactionManager();
        }
        return instance;
    }

//    public void combineOrdersIntoTransaction(SellOrder sellOrder, BuyOrder buyOrder) {
//        BigDecimal currentCourse = sellOrder.getIndex().getCurrentValue();
//        int minimumAmount = Math.min(sellOrder.getNumberOfShares(), buyOrder.getNumberOfShares());
//        int minimumBuyerCanAfford = buyOrder.getAccount().getCurrentState().divideToIntegralValue(currentCourse).intValue();
//        minimumAmount = Math.min(minimumAmount, minimumBuyerCanAfford);
//        BigDecimal totalValue = currentCourse.multiply(BigDecimal.valueOf(minimumAmount));
//        Transaction transaction = new Transaction(buyOrder, sellOrder, sellOrder.getIndex(), currentCourse, totalValue, minimumAmount);
//        sellOrder.getAccount().addSellTransaction(transaction);
//        buyOrder.getAccount().addBuyTransaction(transaction);
//    }
}
