package com.ksptool.entities.mapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

/**
 * JSON 实体映射器的默认实现。
 * 使用 Gson 来序列化和反序列化对象。
 */
public class DefaultJsonEntityMapper implements JsonEntityMapper {

    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    /**
     * 将对象转换为 JSON 字符串。
     *
     * @param obj 要转换的对象
     * @return 对象的 JSON 字符串表示形式，如果对象为null则返回null
     */
    @Override
    public String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return gson.toJson(obj);
    }

    /**
     * 将 JSON 字符串转换为指定类的对象。
     *
     * @param json  要转换的 JSON 字符串
     * @param clazz 要创建的对象的类
     * @param <T>   要创建的对象的类型
     * @return 从 JSON 字符串创建的对象，如果JSON为空或转换失败则返回null
     * @throws RuntimeException 如果JSON格式不正确
     */
    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        if (isBlank(json)) {
            return null;
        }
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 数组字符串转换为指定类的对象列表。
     *
     * @param json  要转换的 JSON 数组字符串
     * @param clazz 列表中对象的类
     * @param <T>   列表中对象的类型
     * @return 从 JSON 数组字符串创建的对象列表，如果JSON为空或转换失败则返回空列表
     */
    @Override
    public <T> List<T> fromJsonArray(String json, Class<T> clazz) {
        if (isBlank(json)) {
            return Collections.emptyList();
        }
        try {
            return gson.fromJson(json, TypeToken.getParameterized(List.class, clazz).getType());
        } catch (JsonSyntaxException e) {
            return Collections.emptyList();
        }
    }

    /**
     * 判断字符串是否为空或空白。
     *
     * @param s 要检查的字符串
     * @return 如果字符串为null、空或只包含空白字符则返回true，否则返回false
     */
    protected boolean isBlank(String s) {
        if (s == null || s.trim().isEmpty()) {
            return true;
        }
        return false;
    }
}
