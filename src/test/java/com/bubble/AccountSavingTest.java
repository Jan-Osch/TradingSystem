
import accounts.entities.Account;
import database.PostgresAccountDao;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountSavingTest {

    public static void main(String[] args) {
//        Account account = new Account(UUID.randomUUID());
//        account.addResources(BigDecimal.valueOf(3115));
//        account.addAsset(UUID.randomUUID(), BigDecimal.valueOf(10));
//        account.addAsset(UUID.randomUUID(), BigDecimal.valueOf(35));
//        account.addAsset(UUID.randomUUID(), BigDecimal.valueOf(100));

        PostgresAccountDao postgresAccountDao = new PostgresAccountDao();

        Account account = postgresAccountDao.getAccountByUuid(UUID.fromString("eae82f4f-947a-420a-9f24-7c653f451b47"));
        account.addResources(BigDecimal.TEN);
    }

}
