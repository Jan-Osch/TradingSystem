package commons;

import markets.exceptions.MarketNotFoundException;

public interface InteractorTransactionWithResult {
    Object execute() throws MarketNotFoundException;
}
