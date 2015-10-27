package models;

import java.math.BigDecimal;
import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        int dateCompare = o1.getExpirationDate().compareTo(o2.getExpirationDate());
        if (dateCompare != 0) {
            return dateCompare;
        }
        BigDecimal value1 = o1.getIndex().getCurrentValue().multiply(BigDecimal.valueOf(o1.getNumberOfShares()));
        BigDecimal value2 = o2.getIndex().getCurrentValue().multiply(BigDecimal.valueOf(o2.getNumberOfShares()));
        return -(value1.compareTo(value2));
    }
}
