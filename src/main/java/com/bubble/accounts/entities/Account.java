package com.bubble.accounts.entities;

import com.bubble.accounts.exceptions.AssetNotSufficient;
import com.bubble.accounts.exceptions.ResourcesNotSufficient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Account {
    private BigDecimal currentBalance;
    private UUID ownerUUID;
    private Map<UUID, BigDecimal> assets;

    public Account(UUID ownerUUID, Map<UUID, BigDecimal> assets, BigDecimal balance) {
        this.ownerUUID = ownerUUID;
        this.assets = assets;
        this.currentBalance = balance;
    }

    public Account(UUID ownerUUID, Map<UUID, BigDecimal> assets) {
        this(ownerUUID, assets, BigDecimal.ZERO);
    }

    public Account(UUID ownerUUID) {
        this(ownerUUID, new HashMap<UUID, BigDecimal>(), BigDecimal.ZERO);
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    private boolean valueCanBeSubtractedFromAccount(BigDecimal valueToSubtract) {
        return this.currentBalance.compareTo(valueToSubtract) != -1;
    }

    public void addResources(BigDecimal amount) {
        this.currentBalance = this.currentBalance.add(amount);
    }

    public void addAsset(UUID instrumentUUID, BigDecimal amount) {
        this.assets.put(instrumentUUID, amount.add(this.getAssetCount(instrumentUUID)));
    }

    public void subtractResources(BigDecimal value) throws ResourcesNotSufficient {
        if (!valueCanBeSubtractedFromAccount(value)) {
            throw new ResourcesNotSufficient();
        }
        this.currentBalance = this.currentBalance.subtract(value);
    }

    public void subtractAsset(UUID instrumentUUID, BigDecimal amount) throws AssetNotSufficient {
        if (this.getAssetCount(instrumentUUID).compareTo(amount) < 0) {
            throw new AssetNotSufficient();
        }
        this.assets.put(instrumentUUID, this.getAssetCount(instrumentUUID).subtract(amount));
    }

    private boolean assetInPortfolio(UUID instrumentUUID) {
        return this.assets.containsKey(instrumentUUID);
    }

    public BigDecimal getAssetCount(UUID instrumentUUID) {
        return this.assets.getOrDefault(instrumentUUID, BigDecimal.ZERO);
    }

    public Map<UUID, BigDecimal> getPortfolio() {
        return assets;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }
}
