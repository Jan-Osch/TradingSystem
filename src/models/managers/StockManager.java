package models.managers;

import models.Index;
import models.Record;
import models.Session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Janusz.
 */
public class StockManager {
    private static StockManager instance = null;
    private ArrayList<Session> historySessions;
    private Session currentSession;
    private Set<Index> indexes;

    private StockManager() {
        historySessions = new ArrayList<>();
        currentSession = new Session();
        indexes = new HashSet<>();
    }

    public static StockManager getInstance() {
        if (instance == null) {
            instance = new StockManager();
        }
        return instance;
    }

    public void changeIndexPrice(Index index, int value){
        if(!this.indexes.contains(index)){
            this.indexes.add(index);
        }
        this.currentSession.createRecord(index, BigDecimal.valueOf(value));
    }

    public BigDecimal getCurrentPrice(Index index){
        return this.currentSession.getCurrentValueOfIndex(index);
    }

    public void startNewSession() {
        this.currentSession.finishSession();
        this.historySessions.add(this.currentSession);
        Session newSession = new Session();
        for(Index index : this.indexes){
            newSession.createRecord(index, this.currentSession.getCurrentValueOfIndex(index));
        }
        this.currentSession = newSession;
    }
}