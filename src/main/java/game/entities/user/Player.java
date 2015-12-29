package game.entities.user;

import game.entities.account.Account;
import markets.entities.instrument.Index;
import game.entities.order.BuyOrder;
import game.entities.order.OrderManager;
import game.entities.order.SellOrder;

import java.math.BigDecimal;
import java.util.Date;

public class Player {
    private Account account;
    private OrderManager orderManager;

    public Player() {
//        this.account = new Account(this);
        this.orderManager = OrderManager.getInstance();
    }

    private Account getAccount() {
        return account;
    }

    public BigDecimal getAccountState() {
        return this.account.getCurrentBalance();
    }

    public void printAccountState() {
        System.out.println("Player's  account state:" + this.getAccountState());
    }

    public void createBuyOrder(Index index, int numberOfShares, int secondsOffset) {
        Date current = new Date();
        Date expirationDate = new Date();
        expirationDate.setSeconds(current.getSeconds() + secondsOffset);
        this.orderManager.addBuyOrder(new BuyOrder(this.getAccount(), index, numberOfShares, expirationDate));
    }

//    public void createSellOrder(Index index, int numberOfShares, int secondsOffset) {
//        if (this.account.getNumberOfSharesInPortfolio(index) == 0) {
//            throw new IllegalArgumentException("Player has not enough shares of given index in his account portfolio");
//        }
//        Date current = new Date();
//        Date expirationDate = new Date();
//        expirationDate.setSeconds(current.getSeconds() + secondsOffset);
//        this.orderManager.addSellOrder(new SellOrder(this.getAccount(), index, numberOfShares, expirationDate));
//    }
//
//    public void printPortfolio() {
////        this.account.printPortfolio();
//    }
//
//    public void addStockToPortfolio(Index index, int amount) {
//        this.account.addStockToPortfolio(index, amount);
//    }
////
//    public void printTotalUserValue() {
//        System.out.println("Player's " + this.name + " total value of stocks + resources in account: " + this.getTotalUserValue());
//    }

//    public BigDecimal getTotalUserValue() {
//        return this.account.getTotalPortfolioValue().addValueOfAnotherRecord(this.getAccountState());
//    }

    public void addResourcesToAccount(int value) {
        this.account.addToAccount(value);
    }

}

