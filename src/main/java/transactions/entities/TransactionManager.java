//package account.transaction;
//
//
//public class TransactionManager {
//    private static TransactionManager instance = null;
//
//    private TransactionManager() {
//    }
//
//    public static TransactionManager getInstance() {
//        if (instance == null) {
//            instance = new TransactionManager();
//        }
//        return instance;
//    }
//
////    public void combineOrdersIntoTransaction(SellOrder sellOrder, BuyOrder buyOrder) {
////        BigDecimal currentCourse = sellOrder.getIndex().getCurrentValue();
////        int minimumAmount = Math.min(sellOrder.getNumberOfShares(), buyOrder.getNumberOfShares());
////        int minimumBuyerCanAfford = buyOrder.getAccount().getCurrentBalance().divideToIntegralValue(currentCourse).intValue();
////        minimumAmount = Math.min(minimumAmount, minimumBuyerCanAfford);
////        BigDecimal totalValue = currentCourse.multiply(BigDecimal.valueOf(minimumAmount));
////        StockTransaction transaction = new StockTransaction(buyOrder, sellOrder, sellOrder.getIndex(), currentCourse, totalValue, minimumAmount);
////        sellOrder.getAccount().addSellTransaction(transaction);
////        buyOrder.getAccount().addBuyTransaction(transaction);
////    }
//}
