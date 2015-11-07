//package bubble.web.simulations;
//
//import models.Index;
//import models.User;
//import models.managers.OrderManager;
//import models.managers.StockManager;
//
//public class Simulations {
//    /**
//     * Create indexes
//     * Create user
//     * Create stockManager
//     * Sets prices of indexes
//     * Add funds to User's account
//     * Check current account state
//     * Add stocks to User's portfolio
//     * Check current portfolio
//     */
//    public static void simpleUserCreation() {
//        System.out.println("########################################");
//        System.out.println("Simple User Creation, adding some stocks");
//
//        Index alior = new Index("ALIOR");
//        Index asseco = new Index("ASSECO");
//        Index enea = new Index("ENEA");
//
//        User andrzejNowak = new User("Andrzej Nowak");
//        andrzejNowak.addResourcesToAccount(1000);
//        andrzejNowak.printAccountState();
//
//        StockManager stockManager = StockManager.getInstance();
//        stockManager.changeIndexPrice(alior, 83);
//        stockManager.changeIndexPrice(asseco, 55);
//        stockManager.changeIndexPrice(enea, 13);
//
//        andrzejNowak.addStockToPortfolio(asseco, 5);
//        andrzejNowak.addStockToPortfolio(alior, 10);
//        andrzejNowak.addStockToPortfolio(enea, 15);
//
//        andrzejNowak.printPortfolio();
//        System.out.println("\n");
//    }
//
//    public static void simpleTransaction() {
//        System.out.println("########################################");
//        System.out.println("Simple transaction");
//        Index alior = new Index("ALIOR");
//        StockManager stockManager = StockManager.getInstance();
//        stockManager.changeIndexPrice(alior, 83);
//
//        User andrzejNowak = new User("Andrzej Nowak");
//        andrzejNowak.addResourcesToAccount(1000);
//        andrzejNowak.addStockToPortfolio(alior, 60);
//
//        User janKowalski = new User("Jan Kowalski");
//        janKowalski.addResourcesToAccount(10000);
//
//        System.out.println("\nBefore Transaction:");
//        janKowalski.printAccountState();
//        janKowalski.printPortfolio();
//        andrzejNowak.printPortfolio();
//        andrzejNowak.printAccountState();
//        andrzejNowak.createSellOrder(alior, 50, 100);
//        janKowalski.createBuyOrder(alior, 100, 100);
//
//        System.out.println("\nAfterTransaction:");
//        janKowalski.printAccountState();
//        janKowalski.printPortfolio();
//        andrzejNowak.printPortfolio();
//        andrzejNowak.printAccountState();
//
//        System.out.println("\n");
//    }
//
//    public static void simpleStocksChangePrice() {
//        System.out.println("########################################");
//        System.out.println("Simple stocks change price");
//        Index alior = new Index("ALIOR");
//        Index pkobp = new Index("PKOBP");
//        Index asseco = new Index("ASSECO");
//        Index enea = new Index("ENEA");
//        StockManager stockManager = StockManager.getInstance();
//        stockManager.changeIndexPrice(alior, 83);
//        stockManager.changeIndexPrice(pkobp, 47);
//        stockManager.changeIndexPrice(asseco, 83);
//        stockManager.changeIndexPrice(enea, 35);
//
//        User andrzejNowak = new User("Andrzej Nowak");
//        andrzejNowak.addResourcesToAccount(1000);
//        andrzejNowak.addStockToPortfolio(alior, 60);
//        andrzejNowak.addStockToPortfolio(enea, 600);
//        andrzejNowak.addStockToPortfolio(asseco, 10);
//        andrzejNowak.addStockToPortfolio(pkobp, 30);
//
//        System.out.println("\nBefore stocks change price:");
//        andrzejNowak.printTotalUserValue();
//
//        stockManager.changeIndexPrice(alior, 82);
//        stockManager.changeIndexPrice(pkobp, 41);
//        stockManager.changeIndexPrice(asseco, 80);
//        stockManager.changeIndexPrice(enea, 22);
//
//        System.out.println("\nAfter stocks change price:");
//        andrzejNowak.printTotalUserValue();
//
//        System.out.println("\n");
//    }
//
//    public static void simpleExpirationDateReached() {
//        System.out.println("########################################");
//        System.out.println("Simple order reached expiration date");
//        Index alior = new Index("ALIOR");
//        StockManager stockManager = StockManager.getInstance();
//        stockManager.changeIndexPrice(alior, 83);
//
//        User andrzejNowak = new User("Andrzej Nowak");
//        andrzejNowak.addResourcesToAccount(1000);
//        andrzejNowak.addStockToPortfolio(alior, 60);
//
//        User janKowalski = new User("Jan Kowalski");
//        janKowalski.addResourcesToAccount(10000);
//
//        System.out.println("\nBefore Transaction:");
//        janKowalski.printAccountState();
//        janKowalski.printPortfolio();
//        andrzejNowak.printPortfolio();
//        andrzejNowak.printAccountState();
//        andrzejNowak.createSellOrder(alior, 50, 10); //10 seconds
//        try {
//            Thread.sleep(11000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("\nAfter more than 61 seconds:");
//        janKowalski.createBuyOrder(alior, 100, 100);
//
//        janKowalski.printAccountState();
//        janKowalski.printPortfolio();
//        andrzejNowak.printPortfolio();
//        andrzejNowak.printAccountState();
//
//        OrderManager orderManager = OrderManager.getInstance();
//        System.out.println("Number of buy orders: " + orderManager.getNumberOfBuyOrdersForIndex(alior));
//        System.out.println("Number of sell orders: " + orderManager.getNumberOfSellOrdersForIndex(alior));
//
//        System.out.println("\n");
//    }
//
//    public static void main(String[] args) {
//        simpleUserCreation();
//        simpleTransaction();
//        simpleStocksChangePrice();
//        simpleExpirationDateReached();
//    }
//}
