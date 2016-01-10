package com.bubble.database;

import com.bubble.markets.entities.instrument.Index;
import com.bubble.persistance.IndexGateWay;

import java.util.UUID;

public class PostgresIndexDao implements IndexGateWay {

    @Override
    public void saveIndex(Index index) {

    }

    @Override
    public Index getIndexByUuid(UUID uuid) {
        return null;
    }

    @Override
    public Iterable<Index> getIndicesByMarketUuid(UUID marketUuid) {
        return null;
    }
}
