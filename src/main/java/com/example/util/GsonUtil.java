/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

/**
 *
 * @author tangyue
 * @version $Id: GsonUtil.java, v 0.1 2019-11-04 13:41 tangyue Exp $$
 */
public final class GsonUtil {

    private static Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder()
                .disableHtmlEscaping().serializeNulls();
        gson = builder.create();
    }

    public static JsonObject fromJson(String json) {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        return jsonObject;
    }

    public static JsonArray toArray(String json) {

        JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
        return jsonArray;
    }

    public static <V> V fromObj(String jsonObj, Class<V> c) {
        return gson.fromJson(jsonObj, c);
    }

    public static <V> V fromObj(String jsonObj, Type type){
        return gson.fromJson(jsonObj, type);
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }
}
