package database;

import application.entities.User;
import persistance.UserGateWay;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PostgresUserDao implements UserGateWay {
    private PostgreSQLJDBC postgreSQLJDBC = PostgreSQLJDBC.getInstance();
    static String tableName = "USERS";

    @Override
    public void saveUser(User user) {
        String sql = "INSERT INTO \":tableName\" (UUID, LOGIN, PASSWORD) VALUES (':uuid', ':login', ':password')";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", user.getUuid().toString());
        sql = SqlUtils.addParameterToSqlStatement(sql, "login", user.getLogin());
        sql = SqlUtils.addParameterToSqlStatement(sql, "password", user.getPassword());
        System.out.println(sql);
        try {
            postgreSQLJDBC.executeSql(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserByUuid(UUID uuid) {
        String sql = "SELECT * FROM \":tableName\" WHERE uuid = ':uuid'";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "uuid", uuid.toString());
        ResultSet resultSet = postgreSQLJDBC.executeSqlWithReturn(sql);
        try {
            resultSet.next();
            String login = resultSet.getString("login");
            String password = resultSet.getString("password");
            postgreSQLJDBC.closeStatementAfterResult(resultSet);
            return new User(login, password, uuid);
        } catch (SQLException e) {
            e.printStackTrace();
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
            postgreSQLJDBC.executeSql(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserByLogin(String login) {
        String sql = "SELECT * FROM \":tableName\" WHERE login = ':login'";
        sql = SqlUtils.addParameterToSqlStatement(sql, "tableName", tableName);
        sql = SqlUtils.addParameterToSqlStatement(sql, "login", login);
        ResultSet resultSet = postgreSQLJDBC.executeSqlWithReturn(sql);
        try {
            resultSet.next();
            String uuid = resultSet.getString("uuid");
            String password = resultSet.getString("password");
            postgreSQLJDBC.closeStatementAfterResult(resultSet);
            return new User(login, password, UUID.fromString(uuid));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
;