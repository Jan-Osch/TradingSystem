package com.bubble.persistance;


import com.bubble.accounts.entities.Account;

import java.util.UUID;

public interface AccountGateWay {
    Account getAccountByUuid(UUID accountUuid);

    void save(Account account);
}

