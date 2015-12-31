package persistance;

import accounts.entities.Account;

public interface AccountGateWay {
    Account getAccountByUuid();

    void save(Account account);
}

