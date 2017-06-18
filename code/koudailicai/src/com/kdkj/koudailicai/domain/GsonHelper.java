package com.kdkj.koudailicai.domain;

import android.util.Log;
import com.google.gson.Gson;
import com.kdkj.koudailicai.util.LogUtil;
import java.lang.reflect.Type;

import org.json.JSONObject;

/**
 * 对gson进行简单封装，方便使用
 * @author liaoheng
 * @creation 2014-12-12
 *
 */
public class GsonHelper {

    private static Boolean hasGson;
    private static Object gson;

    public static boolean hasGson() {
        if (hasGson == null) {
            try {
                Class.forName("com.google.gson.Gson");
                hasGson = true;
            } catch (Exception e) {
                hasGson = false;
            }
        }
        return hasGson;
    }

    public static Gson get() {
        if (gson == null) gson = new Gson();
        return (Gson) gson;
    }

    public static String toJson(Object obj) {
        try {
            return obj == null ? null : get().toJson(obj);
        } catch (Exception e) {
            Log.e("GsonHelper", "Cannot convert object to JSON", e);
            return null;
        }
    }

    /**
     * 供外部调用
     * @param str
     * @param clazz
     * @return
     */
    public static <T> T fromJson(String str, Class<T> clazz) {
        return fromJson(str, (Type) clazz);
    }
    public static <T> T fromJson(JSONObject str, Class<T> clazz) {
        return fromJson(str.toString(), (Type) clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String str, Type type) {
        try {
            return str == null ? null : (T) get().fromJson(str, type);
        } catch (Exception e) {
            LogUtil.e("GsonHelper", "Cannot parse JSON to object", e);
            return null;
        }
    }

}
