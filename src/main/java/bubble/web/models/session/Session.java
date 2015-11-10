package bubble.web.models.session;


import bubble.web.commons.IdHelper;
import bubble.web.models.instrument.Index;
import bubble.web.models.record.Record;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Janusz.
 */
public class Session {
    private int id;
    private Date dateStart;
    private Date dateFinish;
    private State state;
    private LinkedHashMap<Index, ArrayList<Record>> records;

    public Session() {
        this.id = IdHelper.getInstance().getNewId();
        this.dateStart = new Date();
        this.state = State.OPEN;
        this.records = new LinkedHashMap<Index, ArrayList<Record>>();
    }

    public void createRecord(Index index, BigDecimal value) {
        if (this.records.containsKey(index)) {
//            this.records.get(index).add(new Record(index.e, value));
        } else {
            ArrayList<Record> previousRecords = new ArrayList<>();
//            previousRecords.add(new Record(index, value));
            this.records.put(index, previousRecords);
        }
    }

    public void finishSession() {
        this.state = State.FINISHED;
        this.dateFinish = new Date();
    }

    public BigDecimal getCurrentValueOfIndex(Index index) {
        if (this.state == State.FINISHED) {
            throw new IllegalStateException("Session not yet finished");
        }
        if (!this.records.containsKey(index)) {
            throw new IllegalArgumentException("Index not in this session");
        }
//        return this.records.get(index).get(this.records.get(index).size() - 1).getValue();
        return null;
    }
}
