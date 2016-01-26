package com.bubble.database;

import com.bubble.application.entities.User;
import com.bubble.persistance.UserGateWay;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PostgresUserDao implements UserGateWay {
    static String tableName = "USERS";

    @Override
    public void saveUser(User user) {
        String sql = "INSERT INTO \":tableName\" (UUID, LOGIN, PASSWORD, MODERATOR) VALUES (':uuid', ':login', ':password', ':moderator')";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", user.getUuid().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "login", user.getLogin());
        sql = SqlUtils.addParameterToSqlStatement(sql, "password", user.getPassword());
        sql = SqlUtils.addParameterToSqlStatement(sql, "moderator", String.valueOf(user.getModerator()));
        System.out.println(sql);
        try {
            PostgreSQLJDBC.executeSql(sql);
        } catch (SQLException ignored) {
        }
    }

    @Override
    public User getUserByUuid(UUID uuid) {
        String sql = "SELECT * FROM \":tableName\" WHERE uuid = ':uuid'";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", uuid.toString());
        ResultSet resultSet = PostgreSQLJDBC.executeSqlWithReturn(sql);
        try {
            resultSet.next();
            String login = resultSet.getString("login");
            String password = resultSet.getString("password");
            boolean moderator = resultSet.getBoolean("moderator");
            PostgreSQLJDBC.closeStatementAfterResult(resultSet);
            return new User(login, password, uuid, moderator);
        } catch (SQLException ignored) {
        }
        return null;
    }

    @Override
    public void updateUser(User user) {
        deleteUser(user);
        saveUser(user);
    }

    @Override
    public void deleteUser(User user) {
        String sql = "DELETE from \":tableName\" WHERE uuid =':uuid'";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", user.getUuid().toString());
        try {
            PostgreSQLJDBC.executeSql(sql);
        } catch (SQLException ignored) {
        }
    }

    @Override
    public User getUserByLogin(String login) {
        String sql = "SELECT * FROM \":tableName\" WHERE login = ':login'";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "login", login);
        ResultSet resultSet = PostgreSQLJDBC.executeSqlWithReturn(sql);
        try {
            resultSet.next();
            String uuid = resultSet.getString("uuid");
            String password = resultSet.getString("password");
            Boolean moderator = resultSet.getBoolean("moderator");
            PostgreSQLJDBC.closeStatementAfterResult(resultSet);
            return new User(login, password, UUID.fromString(uuid), moderator);
        } catch (SQLException ignored) {
        }
        return null;
    }
}
;