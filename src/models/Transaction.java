package models;

import commons.IdHelper;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Janusz.
 */
public class Transaction {
    private int id = IdHelper.getInstance().getNewId();
    private Order orderIn;
    private Order orderOut;
    private Account accountIn;
    private Account accountOut;
    private Integer numberOfShares;
    private Index index;
    private BigDecimal value;
    private Date dateOfRealisation;

    public Transaction(Order orderIn, Order orderOut) {
        this.id = IdHelper.getInstance().getNewId();
        this.orderIn = orderIn;
        this.orderOut = orderOut;
        this.accountIn = orderIn.

    }

}
