package bubble.web.models.scraper;

import bubble.web.hibernate.HibernateUtil;
import bubble.web.models.instrument.Stock;
import bubble.web.models.record.StockRecord;

public class SavingTest {
    public static void main(String[] args) {
        StockRecord stockRecord = new StockRecord(new Stock("KUPKA", "FULL_KUPKA"),100, 3000);
        HibernateUtil.saveStockRecordToDatabase("GPW_STOCKS_30_SECONDS", stockRecord);
    }

}
