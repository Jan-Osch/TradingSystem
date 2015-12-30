import main.orchestrator.WarsawOrchestrator;
import markets.entities.market.Market;

public class OrchestratorTest {
    public static void main(String[] args) {
        Market warsaw = InitializeApplication.initializeMarket();
        WarsawOrchestrator warsawOrchestrator = new WarsawOrchestrator(warsaw.getUuid());
        warsawOrchestrator.startOrchestratingTheMarket();
    }
}
