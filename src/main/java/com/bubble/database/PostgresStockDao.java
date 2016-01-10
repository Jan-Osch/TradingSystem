package com.bubble.database;

import com.bubble.markets.entities.instrument.Stock;
import com.bubble.persistance.StockGateWay;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
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
        String sql = "SELECT * FROM \":tableName\" WHERE uuid = ':uuid'";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", uuid.toString());
        ResultSet resultSet = PostgreSQLJDBC.executeSqlWithReturn(sql);
        try {
            resultSet.next();
            return parseStockFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Stock parseStockFromResultSet(ResultSet resultSet) throws SQLException {
        String uuid = resultSet.getString("uuid");
        String market_uuid = resultSet.getString("market_uuid");
        String code = resultSet.getString("code");
        String fullName = resultSet.getString("name");
        return new Stock(UUID.fromString(uuid), UUID.fromString(market_uuid), code, fullName);
    }

    @Override
    public Iterable<Stock> getStocksByMarketUuid(UUID marketUuid) {
        String sql = "SELECT * FROM \":tableName\" WHERE market_uuid = ':marketUuid'";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "marketUuid", marketUuid.toString());
        ResultSet resultSet = PostgreSQLJDBC.executeSqlWithReturn(sql);
        List<Stock> result = new LinkedList<>();

        try {
            while (resultSet.next()) {
                result.add(parseStockFromResultSet(resultSet));
            }
            PostgreSQLJDBC.closeStatementAfterResult(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
