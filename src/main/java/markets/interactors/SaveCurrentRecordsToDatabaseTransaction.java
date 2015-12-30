package markets.interactors;

import commons.InteractorTransaction;

import java.util.UUID;

public class SaveCurrentRecordsToDatabaseTransaction implements InteractorTransaction {
    private UUID marketUUid;

    public SaveCurrentRecordsToDatabaseTransaction(UUID marketUUid) {
        this.marketUUid = marketUUid;
    }

    @Override
    public void execute() {

    }
}
