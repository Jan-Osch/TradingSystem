package com.bubble.database;

import com.bubble.accounts.entities.Account;
import com.bubble.commons.JsonHelper;
import com.bubble.persistance.AccountGateWay;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class PostgresAccountDao implements AccountGateWay {

    private PostgreSQLJDBC postgreSQLJDBC = PostgreSQLJDBC.getInstance();
    private static String tableName = "ACCOUNTS";

    private static Map<UUID, BigDecimal> parsePortfolioFromString(String jsonString) {
        return JsonHelper.jsonToMapUuidBigDecimal(jsonString);
    }

    @Override
    public Account getAccountByUuid(UUID accountUuid) {
        String sql = "SELECT * FROM \":tableName\" WHERE uuid = ':uuid'";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", accountUuid.toString());
        ResultSet resultSet = PostgreSQLJDBC.executeSqlWithReturn(sql);
        try {
            resultSet.next();
            String portfolioAsJson = resultSet.getString("assets");
            BigDecimal balance = resultSet.getBigDecimal("balance");
            PostgreSQLJDBC.closeStatementAfterResult(resultSet);
            return new Account(accountUuid, parsePortfolioFromString(portfolioAsJson), balance);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Account account) {
        String sql = "INSERT INTO \":tableName\" VALUES (':uuid', ':balance', ':assets')";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", account.getOwnerUUID().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "balance", account.getCurrentBalance().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "assets", JsonHelper.mapUuidBigDecimalToJson(account.getPortfolio()));
        System.out.println(sql);
        try {
            PostgreSQLJDBC.executeSql(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}