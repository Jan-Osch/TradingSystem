package bubble.web.postgresSQLJDBC;

import bubble.web.models.record.HistoricalStockRecord;
import bubble.web.models.record.StockRecord;
import com.sun.javaws.exceptions.InvalidArgumentException;

import java.sql.*;

public class PostgreSQLJDBC {
    static PostgreSQLJDBC instance = null;
    static Connection connection = null;

    private PostgreSQLJDBC() {
        this.connection = createConnection();
    }

    public static PostgreSQLJDBC getInstance() {
        if (instance == null) {
            instance = new PostgreSQLJDBC();
        }
        return instance;
    }

    private Connection createConnection() {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/bubble_dev",
                            "bubble", "bubble");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        return c;
    }

    private static String addParameterToSqlStatement(String baseSql, String parameterName, String value) {
        return baseSql.replaceFirst(":" + parameterName, value);
    }

    public static void saveStockRecordToDatabase(String tableName, StockRecord stockRecord) {
        try {
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO \":tableName\" (DATE, CODE, VALUE) VALUES (':date', ':code', :value)";

            sql = addParameterToSqlStatement(sql, "tableName", tableName);
            sql = addParameterToSqlStatement(sql, "date", stockRecord.getDateCreated().toGMTString());
            sql = addParameterToSqlStatement(sql, "code", stockRecord.getInstrument().getCodeName());
            sql = addParameterToSqlStatement(sql, "value", stockRecord.getValue().toString());
//            System.out.println("SQL QUERY: "+ sql);

            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveHistoricalStockRecordToDatabase(String tableName, HistoricalStockRecord historicalStockRecord) {
        try {
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO \":tableName\" (DATE, CODE, VALUE, VOLUME, OPENING, MINIMUM, CHANGE, TRANSACTIONS) "
                    + "VALUES (':date', ':code', :value, :volume, :opening, :minimum, :change, :transactions)";

            sql = addParameterToSqlStatement(sql, "tableName", tableName);
            sql = addParameterToSqlStatement(sql, "date", historicalStockRecord.getDateCreated().toGMTString());
            sql = addParameterToSqlStatement(sql, "code", historicalStockRecord.getInstrument().getCodeName());
            sql = addParameterToSqlStatement(sql, "value", historicalStockRecord.getValue().toString());
            sql = addParameterToSqlStatement(sql, "volume", historicalStockRecord.getVolumeInShares().toString());
            sql = addParameterToSqlStatement(sql, "opening", historicalStockRecord.getOpeningValue().toString());
            sql = addParameterToSqlStatement(sql, "minimum", historicalStockRecord.getMinimumValue().toString());
            sql = addParameterToSqlStatement(sql, "change", String.valueOf(historicalStockRecord.getValueChangePercentage()));
            sql = addParameterToSqlStatement(sql, "transactions", String.valueOf(historicalStockRecord.getNumberOfTransactions()));

//            System.out.println("SQL QUERY: "+ sql);

            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}