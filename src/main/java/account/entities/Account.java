package account.entities.account;

import account.portfolio.Portfolio;
import account.user.Player;
import markets.entities.market.Market;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private final Player owner;
    private BigDecimal currentBalance;
    private List<AccountTransaction> historyOfAccountTransactions;
    private Portfolio portfolio;

    public Account(Player owner, Market market) {
        this.owner = owner;
        this.currentBalance = BigDecimal.valueOf(0);
        this.historyOfAccountTransactions = new ArrayList<>();
//        this.portfolio = PortfolioFactory.createPortfolio(market);
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void addToAccount(BigDecimal valueToAdd) {
        this.currentBalance = this.currentBalance.add(valueToAdd);
    }

    public void addToAccount(int value) {
        this.currentBalance = this.currentBalance.add(BigDecimal.valueOf(value));
    }

    void subtractFromAccount(BigDecimal valueToSubtract) throws BalanceNotSufficient {
        if (!valueCanBeSubtractedFromAccount(valueToSubtract)) {
            throw new BalanceNotSufficient();
        }
        this.currentBalance = this.currentBalance.subtract(valueToSubtract);
    }

    private boolean valueCanBeSubtractedFromAccount(BigDecimal valueToSubtract) {
        return this.currentBalance.compareTo(valueToSubtract) != -1;
    }

    public void addInFlowTransaction(InFlowTransaction inFlowTransaction) {
        this.addToAccount(inFlowTransaction.getValue());
        this.historyOfAccountTransactions.add(inFlowTransaction);
    }

    public void addOutFlowTransaction(OutFlowTransaction outFlowTransaction) throws BalanceNotSufficient {
        this.subtractFromAccount(outFlowTransaction.getValue());
        this.historyOfAccountTransactions.add(outFlowTransaction);
    }

    private class BalanceNotSufficient extends Throwable {
    }
}
