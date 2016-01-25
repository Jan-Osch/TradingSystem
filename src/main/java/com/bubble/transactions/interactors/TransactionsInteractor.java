package com.bubble.transactions.interactors;

import com.bubble.accounts.exceptions.AccountUuidNotFound;
import com.bubble.accounts.exceptions.AssetNotSufficient;
import com.bubble.accounts.exceptions.ResourcesNotSufficient;
import com.bubble.accounts.interactors.AccountsInteractor;
import com.bubble.markets.exceptions.InstrumentUuidNotFoundException;
import com.bubble.markets.exceptions.MarketNotFoundException;
import com.bubble.markets.interactors.MarketsInteractor;
import com.bubble.persistance.EntityGateWayManager;
import com.bubble.persistance.TransactionGateWay;
import com.bubble.transactions.entities.Transaction;
import com.bubble.transactions.entities.TransactionManager;
import com.bubble.transactions.entities.TransactionType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class TransactionsInteractor {

    private static TransactionManager transactionManager = TransactionManager.getInstance();
    private static TransactionGateWay transactionGateWay = EntityGateWayManager.getTransactionGateWay();

    public static void createStockBuyOrderByMarketPrice(UUID instrumentUUID, UUID accountUUID, BigDecimal amount) throws AccountUuidNotFound, MarketNotFoundException, InstrumentUuidNotFoundException, ResourcesNotSufficient {
        BigDecimal balanceForAccount = AccountsInteractor.getBalanceForAccount(accountUUID);
        BigDecimal currentStockValue = MarketsInteractor.getCurrentStockValue(instrumentUUID);
        BigDecimal amountOfShares = transactionManager.getAmountOfShares(balanceForAccount, currentStockValue, amount);
        BigDecimal finalValue = amountOfShares.multiply(currentStockValue);
        createAndSaveTransaction(finalValue, accountUUID, instrumentUUID, TransactionType.BUY);
        AccountsInteractor.performBuyTransactionForAccount(accountUUID, instrumentUUID, amountOfShares, finalValue);
    }

    public static void createStockSellOrderByMarketPrice(UUID instrumentUUID, UUID accountUUID, BigDecimal amount) throws AccountUuidNotFound, MarketNotFoundException, InstrumentUuidNotFoundException, AssetNotSufficient {
        BigDecimal currentCount = AccountsInteractor.getAssetCountForAccount(accountUUID, instrumentUUID);
        BigDecimal actualCount = currentCount.min(amount);
        BigDecimal value = MarketsInteractor.getCurrentStockValue(instrumentUUID);
        createAndSaveTransaction(value, accountUUID, instrumentUUID, TransactionType.SELL);
        AccountsInteractor.performSellTransactionForAccount(accountUUID, instrumentUUID, actualCount, value);
    }

    private static void createAndSaveTransaction(BigDecimal value, UUID accountUUID, UUID instrumentUUID, TransactionType type) {
        Transaction transaction = new Transaction(accountUUID, UUID.randomUUID(), value, instrumentUUID, type, new Date());
        transactionGateWay.saveTransaction(transaction);
    }

    public static BigDecimal getTotalValueOfAccount(UUID accountUUID) throws AccountUuidNotFound, MarketNotFoundException, InstrumentUuidNotFoundException {
        Map<UUID, BigDecimal> portfolio = AccountsInteractor.getPortfolio(accountUUID);
        BigDecimal total = AccountsInteractor.getBalanceForAccount(accountUUID);
        BigDecimal partial;
        for (UUID uuid : portfolio.keySet()) {
            partial = MarketsInteractor.getCurrentStockValue(uuid);
            total = total.add(partial.multiply(portfolio.get(uuid)));
        }
        return total;
    }

    public static Iterable<Transaction> getTransactionsForUser(UUID accountUUID) {
        return transactionGateWay.getTransactionsForUser(accountUUID);
    }
}
