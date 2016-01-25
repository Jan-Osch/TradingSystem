package com.bubble.transactions.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Transaction {
    private final UUID ownerUuid;
    private final UUID uuid;
    private final BigDecimal value;
    private final UUID instrumentUuid;
    private TransactionType type;
    private Date date;

    public Transaction(UUID ownerUuid, UUID uuid, BigDecimal value, UUID instrumentUuid, TransactionType type, Date date) {
        this.ownerUuid = ownerUuid;
        this.uuid = uuid;
        this.value = value;
        this.instrumentUuid = instrumentUuid;
        this.type = type;
        this.date = date;
    }

    public UUID getUuid() {
        return uuid;
    }

    public BigDecimal getValue() {
        return value;
    }

    public UUID getInstrumentUuid() {
        return instrumentUuid;
    }

    public TransactionType getType() {
        return type;
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
