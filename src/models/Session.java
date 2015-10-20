package models;

import java.util.Date;
import java.util.LinkedHashSet;

/**
 * @author Janusz.
 */
public class Session {
    private int id;
    private Date dateStart;
    private Date dateFinish;
    private LinkedHashSet<Index> indexes;
}
