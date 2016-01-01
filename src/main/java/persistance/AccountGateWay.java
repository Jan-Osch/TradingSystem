package persistance;

import accounts.entities.Account;

import java.util.UUID;

public interface AccountGateWay {
    Account getAccountByUuid(UUID accountUuid);

    void save(Account account);
}

