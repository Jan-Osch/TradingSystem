//package account.order;
//
//import account.transaction.TransactionManager;
//import markets.entities.instrument.Index;
//
//import java.util.*;
//
//public class OrderManager {
//    private static OrderManager instance = null;
//    private TreeMap<Index, TreeSet<BuyOrder>> buyOrders;
//    private TreeMap<Index, TreeSet<SellOrder>> sellOrders;
//    private TransactionManager transactionManager;
//
//    private OrderManager() {
//        buyOrders = new TreeMap<Index,TreeSet<BuyOrder>>();
//        sellOrders = new TreeMap<Index, TreeSet<SellOrder>>();
//        transactionManager = TransactionManager.getInstance();
//    }
//
//    public static OrderManager getInstance(){
//        if (instance == null){
//            instance = new OrderManager();
//        }
//        return instance;
//    }
//
//    public BuyOrder findMatchingBuyOrder(SellOrder sellOrder){
//        if (!this.buyOrders.containsKey(sellOrder.getIndex())){
//            return null;
//        }
//        Iterator iterator = this.buyOrders.get(sellOrder.getIndex()).descendingIterator();
//        BuyOrder order;
//        Date currentDate = new Date();
//        while(iterator.hasNext()){
//            order = (BuyOrder) iterator.next();
//            if(order.getExpirationDate().compareTo(currentDate) == -1){
//                this.buyOrders.get(sellOrder.getIndex()).remove(order);
//            }
//            else{
//                return order;
//            }
//        }
//        return null;
//    }
//
//    public SellOrder findMatchingSellOrder(BuyOrder buyOrder){
//        if (!this.sellOrders.containsKey(buyOrder.getIndex())){
//            return null;
//        }
//        Iterator iterator = this.sellOrders.get(buyOrder.getIndex()).descendingIterator();
//        SellOrder order;
//        Date currentDate = new Date();
//        while(iterator.hasNext()){
//            order = (SellOrder) iterator.next();
//            if(order.getExpirationDate().compareTo(currentDate) == -1){
//                this.sellOrders.get(buyOrder.getIndex()).remove(order);
//            }
//            else{
//                this.sellOrders.get(buyOrder.getIndex()).remove(order);
//                return order;
//            }
//        }
//        return null;
//    }
//
//    public void addSellOrder(SellOrder sellOrder){
//        BuyOrder buyOrder = this.findMatchingBuyOrder(sellOrder);
//        if(buyOrder!= null){
////            this.transactionManager.combineOrdersIntoTransaction(sellOrder, buyOrder);
//            return;
//        }
//        if (!this.sellOrders.containsKey(sellOrder.getIndex())){
////            this.sellOrders.put(sellOrder.getIndex(), new TreeSet<>(new OrderComparator()));
//        }
//        this.sellOrders.get(sellOrder.getIndex()).add(sellOrder);
//    }
//
//    public void addBuyOrder(BuyOrder buyOrder){
//        SellOrder sellOrder= this.findMatchingSellOrder(buyOrder);
//        if(sellOrder!= null){
////            this.transactionManager.combineOrdersIntoTransaction(sellOrder, buyOrder);
//            return;
//        }
//        if (!this.buyOrders.containsKey(buyOrder.getIndex())){
////            this.buyOrders.put(buyOrder.getIndex(), new TreeSet<>(new OrderComparator()));
//        }
//        this.buyOrders.get(buyOrder.getIndex()).add(buyOrder);
//    }
//
//    public int getNumberOfBuyOrdersForIndex(Index index){
//        if(!this.buyOrders.containsKey(index)){
//            return 0;
//        }
//        return this.buyOrders.get(index).size();
//    }
//
//
//    public int getNumberOfSellOrdersForIndex(Index index){
//        if(!this.sellOrders.containsKey(index)){
//            return 0;
//        }
//        return this.sellOrders.get(index).size();
//    }
//}
