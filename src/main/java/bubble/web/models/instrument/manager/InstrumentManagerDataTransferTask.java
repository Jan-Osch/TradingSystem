package bubble.web.models.instrument.manager;

import java.util.TimerTask;

public class InstrumentManagerDataTransferTask extends TimerTask {

    private final InstrumentManager instrumentManager;

    public InstrumentManagerDataTransferTask(InstrumentManager instrumentManager) {
        this.instrumentManager = instrumentManager;
    }

    @Override
    public void run() {
        this.instrumentManager.transferData();
    }
}
