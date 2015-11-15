package bubble.web.hibernate;


import bubble.web.models.record.StockRecord;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;


public class HibernateUtil {

    /** Creates and sets a Session factory to perform queries with. */
    private static final File hibernateConfigurationFile = new File("src/main/java/bubble/web/hibernate/hibernate.cfg.xml");
    private static final SessionFactory sessionFactory = buildSessionFactory();
//    private static final File hibernateConfigurationFile = new File("src/main/java/bubble.web/hibernate/hibernate.cfg.xml");
    /** Creates a new session factory based on the hibernate config (xml) file.
     * @return The factory.
     */
    private static SessionFactory buildSessionFactory() {
        System.out.println(hibernateConfigurationFile.length());

        try {
            return new Configuration().configure(hibernateConfigurationFile).buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /** @return The session factory. */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void saveStockRecordToDatabase(String databaseName, StockRecord record){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("INSERT INTO :table (date, code, value, volume) VALUES (:_date, :_code, :_value, :_volume");
        query.setParameter("table", databaseName);
        query.setParameter("_date", record.getDateCreated());
        query.setParameter("_code", record.getInstrument().getCodeName());
        query.setParameter("_value", record.getValue());
        query.setParameter("_volume", record.getVolume());
        session.beginTransaction();
        query.uniqueResult();
        session.close();
    }

}