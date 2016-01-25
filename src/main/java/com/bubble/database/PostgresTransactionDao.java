package com.bubble.database;

import com.bubble.persistance.TransactionGateWay;
import com.bubble.transactions.entities.Transaction;
import com.bubble.transactions.entities.TransactionType;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class PostgresTransactionDao implements TransactionGateWay {

    private String tableName = "transactions";

    @Override
    public void saveTransaction(Transaction transaction) {
        String sql = "INSERT INTO \":tableName\" VALUES (':uuid', ':owner', ':value', ':instrument', ':type', ':date')";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", transaction.getUuid().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "owner", transaction.getOwnerUuid().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "value", transaction.getValue().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "instrument", transaction.getInstrumentUuid().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "type", transaction.getType().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "date", transaction.getDate().toString());
        try {
            PostgreSQLJDBC.executeSql(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Transaction getTransactionByUuid(UUID uuid) {
        String sql = "SELECT * FROM \":tableName\" WHERE uuid = ':uuid'";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", uuid.toString());
        ResultSet resultSet = PostgreSQLJDBC.executeSqlWithReturn(sql);
        try {
            resultSet.next();
            return parseTransactionFromResultSet(resultSet);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Iterable<Transaction> getTransactionsForUser(UUID userUuid) {
        String sql = "SELECT * FROM \":tableName\" WHERE owner = ':uuid'";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", userUuid.toString());
        ResultSet resultSet = PostgreSQLJDBC.executeSqlWithReturn(sql);
        ArrayList<Transaction> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                result.add(parseTransactionFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            return result;
        }
        return result;
    }

    private Transaction parseTransactionFromResultSet(ResultSet resultSet) throws SQLException {
        String uuid = resultSet.getString("uuid");
        String owner = resultSet.getString("owner");
        BigDecimal value = resultSet.getBigDecimal("value");
        String instrument = resultSet.getString("instrument");
        String type = resultSet.getString("type");
        Date date = resultSet.getTimestamp("date");
        return new Transaction(UUID.fromString(owner), UUID.fromString(uuid), value, UUID.fromString(instrument), TransactionType.valueOf(type), date);
    }
}