package bubble.web.models.user;

import bubble.web.commons.IdHelper;
import bubble.web.models.account.Account;
import bubble.web.models.index.Index;
import bubble.web.models.order.BuyOrder;
import bubble.web.models.order.OrderManager;
import bubble.web.models.order.SellOrder;

import java.math.BigDecimal;
import java.util.Date;

public class User {
    private final String name;
    private final int id;
    private Account account;
    private OrderManager orderManager;

    public User(String name) {
        this.id = IdHelper.getInstance().getNewId();
        this.name = name;
        this.account = new Account(this);
        this.orderManager = OrderManager.getInstance();
    }

    private Account getAccount() {
        return account;
    }

    public BigDecimal getAccountState() {
        return this.account.getCurrentState();
    }

    public void printAccountState() {
        System.out.println("User's " + this.name + " account state: " + this.getAccountState());
    }

    public void createBuyOrder(Index index, int numberOfShares, int secondsOffset) {
        Date current = new Date();
        Date expirationDate = new Date();
        expirationDate.setSeconds(current.getSeconds() + secondsOffset);
        this.orderManager.addBuyOrder(new BuyOrder(this.getAccount(), index, numberOfShares, expirationDate));
    }

    public void createSellOrder(Index index, int numberOfShares, int secondsOffset) {
        if (this.account.getNumberOfSharesInPortfolio(index) == 0) {
            throw new IllegalArgumentException("User has not enough shares of given index in his account portfolio");
        }
        Date current = new Date();
        Date expirationDate = new Date();
        expirationDate.setSeconds(current.getSeconds() + secondsOffset);
        this.orderManager.addSellOrder(new SellOrder(this.getAccount(), index, numberOfShares, expirationDate));
    }

    public void printPortfolio() {
        System.out.println("User's " + this.name + " portfolio:");
        this.account.printPortfolio();
    }

    public void addStockToPortfolio(Index index, int amount) {
        this.account.addStockToPortfolio(index, amount);
    }

    public void printTotalUserValue() {
        System.out.println("User's " + this.name + " total value of stocks + resources in account: " + this.getTotalUserValue());
    }

    public BigDecimal getTotalUserValue() {
        return this.account.getTotalPortfolioValue().add(this.getAccountState());
    }

    public void addResourcesToAccount(int value) {
        this.account.addToAccount(value);
    }

    public int getId() {
        return id;
    }
}

