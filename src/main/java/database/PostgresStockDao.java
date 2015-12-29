package database;

import markets.entities.instrument.Stock;
import persistance.StockGateWay;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PostgresStockDao implements StockGateWay {
    private String tableName = "STOCKS";
    private PostgreSQLJDBC postgreSQLJDBC = PostgreSQLJDBC.getInstance();

    @Override
    public void saveStock(Stock stock) {
        String sql = "INSERT INTO \":tableName\" (UUID, MARKET_UUID, CODE, NAME) VALUES (':uuid', ':marketUuid', ':code', ':name')";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", stock.getUuid().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "marketUuid", stock.getMarketUuid().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "code", stock.getCodeName());
        sql = SqlUtils.addParameterToSqlStatement(sql, "name", stock.getFullName());
        System.out.println(sql);
        try {
            postgreSQLJDBC.executeSql(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Stock getStockByUuid(UUID uuid) {
        return null;
    }

    @Override
    public List<Stock> getStocksByMarketUuid(UUID marketUuid) {
        return null;
    }
}
