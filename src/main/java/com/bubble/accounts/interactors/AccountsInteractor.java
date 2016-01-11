package com.bubble.accounts.interactors;

import com.bubble.accounts.entities.Account;
import com.bubble.accounts.exceptions.AccountUuidNotFound;
import com.bubble.accounts.exceptions.AssetNotSufficient;
import com.bubble.accounts.exceptions.OwnerAlreadyHasAccount;
import com.bubble.accounts.exceptions.ResourcesNotSufficient;
import com.bubble.persistance.AccountGateWay;
import com.bubble.persistance.EntityGateWayManager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountsInteractor {

    private static AccountGateWay accountGateWay = EntityGateWayManager.getAccountGateWay();

    public static Account getAccount(UUID ownerUUID) throws AccountUuidNotFound {
        Account account = accountGateWay.getAccountByUuid(ownerUUID);
        if (account == null) {
            throw new AccountUuidNotFound();
        }
        return account;
    }

    public static Map<UUID, BigDecimal> getPortfolio(UUID ownerUUID) throws AccountUuidNotFound {
        Account account = getAccount(ownerUUID);
        return account.getPortfolio();
    }

    protected static void addResourcesToAccount(UUID ownerUUID, BigDecimal amount) throws AccountUuidNotFound {
        Account account = getAccount(ownerUUID);
        account.addResources(amount);
        accountGateWay.update(account);
    }

    protected static void subtractResources(UUID ownerUUID, BigDecimal amount) throws AccountUuidNotFound, ResourcesNotSufficient {
        Account account = getAccount(ownerUUID);
        account.subtractResources(amount);
        accountGateWay.update(account);
    }

    protected static void addAsset(UUID ownerUUID, UUID instrumentUUID, BigDecimal amount) throws AccountUuidNotFound {
        Account account = getAccount(ownerUUID);
        account.addAsset(instrumentUUID, amount);
        accountGateWay.update(account);
    }

    protected static void subtractAsset(UUID ownerUUID, UUID instrumentUUID, BigDecimal amount) throws AccountUuidNotFound, AssetNotSufficient {
        Account account = getAccount(ownerUUID);
        account.subtractAsset(instrumentUUID, amount);
        accountGateWay.update(account);
    }

    public static void createAccount(UUID ownerUUID, BigDecimal initalBalance) throws OwnerAlreadyHasAccount {
        try {
            getAccount(ownerUUID);
        } catch (AccountUuidNotFound accountUuidNotFound) {
            accountGateWay.save(new Account(ownerUUID, new HashMap<UUID, BigDecimal>(), initalBalance));
            return;
        }
        throw new OwnerAlreadyHasAccount();
    }

    public static BigDecimal getBalanceForAccount(UUID ownerUUID) throws AccountUuidNotFound {
        Account account = getAccount(ownerUUID);
        return account.getCurrentBalance();
    }

    public static BigDecimal getAssetCountForAccount(UUID ownerUUID, UUID instrumentUUID) throws AccountUuidNotFound {
        Account account = getAccount(ownerUUID);
        return account.getAssetCount(instrumentUUID);
    }

    public static void performBuyTransactionForAccount(UUID ownerUUID, UUID instrumentUUID, BigDecimal amount, BigDecimal value) throws AccountUuidNotFound, ResourcesNotSufficient {
        subtractResources(ownerUUID, value);
        addAsset(ownerUUID, instrumentUUID, amount);
    }

    public static void performSellTransactionForAccount(UUID ownerUUID, UUID instrumentUUID, BigDecimal amount, BigDecimal value) throws AccountUuidNotFound, AssetNotSufficient {
        addResourcesToAccount(ownerUUID, value);
        subtractAsset(ownerUUID, instrumentUUID, amount);
    }


}
