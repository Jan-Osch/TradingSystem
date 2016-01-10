package com.bubble.database;


import java.sql.*;
import java.util.HashMap;

public class PostgreSQLJDBC {
    static PostgreSQLJDBC instance = null;
    static Connection connection = null;
    static HashMap<ResultSet, Statement> statementsToClose;

    private PostgreSQLJDBC() {
        connection = createConnection();
        statementsToClose = new HashMap<>();
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

    public static void executeSql(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

    public static PreparedStatement getNewPreparedStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public static void executePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.execute();
        preparedStatement.close();
    }

    public static ResultSet executeSqlWithReturn(String sql) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            statementsToClose.put(resultSet, statement);
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeStatementAfterResult(ResultSet resultSet) throws SQLException {
        Statement statement = statementsToClose.get(resultSet);
        resultSet.close();
        statement.close();
    }

}