import main.orchestrator.WarsawOrchestrator;
import markets.entities.market.Market;
import markets.entities.record.StockRecord;
import markets.interactors.GetCurrentRecordsForPeriodTransaction;
import persistance.CurrentStockRecordGateWay;

import java.util.Date;
import java.util.UUID;

public class OrchestratorTest {
    public static void main(String[] args) {
        Market warsaw = InitializeApplication.initializeMarket();
        WarsawOrchestrator warsawOrchestrator = new WarsawOrchestrator(warsaw.getUuid());
        warsawOrchestrator.startOrchestratingTheMarket();
        try {
            Date twoMinutesAgo = new Date();
            twoMinutesAgo.setTime(twoMinutesAgo.getTime() - 10 * 60 * 10000);

            GetCurrentRecordsForPeriodTransaction getCurrentRecordsForPeriodTransaction = new GetCurrentRecordsForPeriodTransaction(
                    UUID.fromString("692f942f-b51c-4323-8212-d3923175b803"),
                    UUID.fromString("d93e338a-6d0c-4ae7-a730-f84a22eac0cc"),
                    twoMinutesAgo,
                    new Date()

            );
            Iterable<StockRecord> result = getCurrentRecordsForPeriodTransaction.execute();
            System.out.println("finished");

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
