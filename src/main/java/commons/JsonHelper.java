package commons;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class JsonHelper {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private static final Type typeTokenMapUuidToBigDecimal = new TypeToken<Map<UUID, BigDecimal>>() {
    }.getType();
    private static final Type typeTokenMapStringString = new TypeToken<Map<String, String>>() {
    }.getType();

    public static Map<UUID, BigDecimal> jsonToMapUuidBigDecimal(String json) {
        if (json == null || json.isEmpty())
            return new HashMap<UUID, BigDecimal>();
        return gson.fromJson(json, typeTokenMapUuidToBigDecimal);
    }

    public static String mapUuidBigDecimalToJson(Map<UUID, BigDecimal> map) {
        if (map == null)
            map = new HashMap<UUID, BigDecimal>();
        return gson.toJson(map);
    }

    public static Map<String, String> jsonToMapStringString(String json) {
        if (json == null || json.isEmpty())
            return new HashMap<String, String>();
        return gson.fromJson(json, typeTokenMapStringString);
    }
}