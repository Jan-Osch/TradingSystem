package com.bubble.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class SqlUtils {
    public static String addParameterToSqlStatement(String baseSql, String parameterName, String value) {
        return baseSql.replaceFirst(":" + parameterName, value);
    }

    private static String dateFormat = "YYYY-MM-dd";
    private static SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
    private static String dateTimeFormat = "yyyy/MM/dd-HH:mm:ss";
    private static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(dateTimeFormat);

    public static Date dateTimeFromString(String dateString) {
        try {
            return dateTimeFormatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDate(Date date) {
        return formatter.format(date);
    }

    public static String sqlLiteralStringFromList(List<UUID> list) {
        String result = "{";
        for (UUID uuid : list) {
            result = result + uuid.toString() + ",";
        }
        result = result.replaceAll(",$", "");
        return result + "}";
    }

    public static List<UUID> getListFromString(String list) {
        LinkedList<UUID> uuids = new LinkedList<>();
        String[] splitted = list.split(",");
        for (String s : splitted) {
            s = s.replace("{", "");
            s = s.replace("}", "");
            if (!s.equals("")) {
                uuids.add(UUID.fromString(s));
            }
        }
        return uuids;
    }

}
