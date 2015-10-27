package models;

import commons.IdHelper;
import models.managers.StockManager;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Janusz.
 */
public class Index implements Comparable{
    private final int id;
    private final String name;
    private StockManager stockManager;

    public Index(String name) {
        this.id = IdHelper.getInstance().getNewId();
        this.stockManager = StockManager.getInstance();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getCurrentValue(){
        return this.stockManager.getCurrentPrice(this);

    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Object o) {
        if (o.getClass()==this.getClass()){
            Index object = (Index) o;
            if(object.getId() == this.id && Objects.equals(object.getName(), this.name)){
                return 0;
            }
            return object.getName().compareTo(this.name);
        }
        return -1;
    }
}
