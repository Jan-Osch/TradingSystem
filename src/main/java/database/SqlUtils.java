package database;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SqlUtils {
    public static String addParameterToSqlStatement(String baseSql, String parameterName, String value) {
        return baseSql.replaceFirst(":" + parameterName, value);
    }
    private static String dateFormat = "YYYY-MM-dd";
    private static SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

    public static String formatDate(Date date){
        return formatter.format(date);
    }
}
