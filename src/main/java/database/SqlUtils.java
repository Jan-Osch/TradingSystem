package database;

public class SqlUtils {
    public static String addParameterToSqlStatement(String baseSql, String parameterName, String value) {
        return baseSql.replaceFirst(":" + parameterName, value);
    }
}
