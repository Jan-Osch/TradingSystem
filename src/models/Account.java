package models;

import commons.IdHelper;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * @author Janusz.
 */
public class Account {
    private int id;
    private User owner;
    private BigDecimal currentState;
    private Queue<Order> orders;

    /**
     * @param owner
     */
    public Account(User owner) {
        this.id = IdHelper.getInstance().getNewId();
        this.owner = owner;
        this.currentState = BigDecimal.valueOf(0);
        this.orders = new ArrayDeque<>();
    }

    public void addOrderToQueue(Order order){
        this.orders.add(order);
    }
}
