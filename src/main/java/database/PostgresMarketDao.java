package database;

import application.entities.User;
import markets.entities.market.Market;
import persistance.MarketGateWay;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostgresMarketDao implements MarketGateWay {
    private String tableName = "MARKETS";
    private PostgreSQLJDBC postgreSQLJDBC = PostgreSQLJDBC.getInstance();

    @Override
    public void saveMarket(Market market) {
        String sql = "INSERT INTO \":tableName\" (UUID, NAME) VALUES (':uuid', ':name')";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", market.getUuid().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "name", market.getName());
        System.out.println(sql);
        try {
            postgreSQLJDBC.executeSql(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Market getMarketByUuid(UUID uuid) {
        String sql = "SELECT * FROM \":tableName\" WHERE uuid = ':uuid'";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", uuid.toString());
        ResultSet resultSet = postgreSQLJDBC.executeSqlWithReturn(sql);
        try {
            resultSet.next();
            String marketName = resultSet.getString("name");
            postgreSQLJDBC.closeStatementAfterResult(resultSet);
            return new Market(marketName, uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Market> getAllMarkets() {
        List<Market> result = new ArrayList<>();
        String sql = "SELECT * FROM \":tableName\"";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        ResultSet resultSet = postgreSQLJDBC.executeSqlWithReturn(sql);
        try {
            while (resultSet.next()) {
                String marketName = resultSet.getString("name");
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                Market market = new Market(marketName, uuid);
                result.add(market);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
