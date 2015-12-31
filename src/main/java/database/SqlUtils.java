package database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SqlUtils {
    public static String addParameterToSqlStatement(String baseSql, String parameterName, String value) {
        return baseSql.replaceFirst(":" + parameterName, value);
    }
    private static String dateFormat = "YYYY-MM-dd";
    private static SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
    private static String dateTimeFormat = "yyyy/MM/dd-HH:mm:ss";
    private static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(dateTimeFormat);

    public static Date dateTimeFromString(String dateString){
        try {
            return dateTimeFormatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String formatDate(Date date){
        return formatter.format(date);
    }


}
