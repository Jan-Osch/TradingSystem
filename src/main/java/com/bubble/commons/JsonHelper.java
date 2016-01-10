package com.bubble.commons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

public class JsonHelper {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private static final Type typeTokenMapUuidToBigDecimal = new TypeToken<Map<UUID, BigDecimal>>() {
    }.getType();
    private static final Type typeTokenMapUuidUuid = new TypeToken<Map<UUID, UUID>>() {
    }.getType();
    private static final Type typeTokenMapStringString = new TypeToken<Map<String, String>>() {
    }.getType();
    private static final Type typeTokenListUuid = new TypeToken<List<UUID>>() {
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

    public static Map<UUID, UUID> jsonToMapUuidUuid(String json) {
        if (json == null || json.isEmpty())
            return new HashMap<>();
        return gson.fromJson(json, typeTokenMapUuidUuid);
    }

    public static String mapUuidUuidToJson(Map<UUID, UUID> map) {
        if (map == null)
            map = new HashMap<UUID, UUID>();
        return gson.toJson(map);
    }

    public static String listUuidToJson(List<UUID> list) {
        if (list == null) {
            list = new LinkedList<UUID>();
        }
        return gson.toJson(list);
    }

    public static List<UUID> jsonToListUuid(String json) {
        if (json == null || json.isEmpty()) {
            return new LinkedList<UUID>();
        }
        return gson.fromJson(json, typeTokenListUuid);
    }

}