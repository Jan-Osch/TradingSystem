package markets.interactors;

import commons.InteractorTransaction;
import markets.entities.instrument.Stock;
import persistance.EntityGateWayManager;
import persistance.StockGateWay;

public class CreateStockTransaction implements InteractorTransaction {
    private Stock stock;

    public CreateStockTransaction(Stock stock) {
        this.stock = stock;
    }

    @Override
    public void execute() {
        StockGateWay stockGateWay = EntityGateWayManager.getStockGateWay();
        stockGateWay.saveStock(this.stock);
    }
}
