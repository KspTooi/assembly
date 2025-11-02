package com.ksptool.entities;

import com.ksptool.entities.mapper.EntityMapper;
import com.ksptool.entities.mapper.JsonEntityMapper;
import com.ksptool.entities.utils.Strings;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供用于处理实体的实用方法，包括对象映射、JSON 序列化和反序列化。
 * 直接使用接口的全局默认实例。
 */
public class Entities {

    /**
     * 返回用于对象映射的 {@link EntityMapper}。
     *
     * @return {@link EntityMapper} 实例
     */
    public static EntityMapper getObjectMapper() {
        return EntityMapper.Global.getDefault();
    }

    /**
     * 设置用于对象映射的 {@link EntityMapper}。
     *
     * @param m 要设置的 {@link EntityMapper} 实例
     */
    public static void setObjectMapper(EntityMapper m) {
        EntityMapper.Global.setDefault(m);
    }

    /**
     * 设置用于 JSON 序列化和反序列化的 {@link JsonEntityMapper}。
     *
     * @param m 要设置的 {@link JsonEntityMapper} 实例
     */
    public static void setJsonEntityMapper(JsonEntityMapper m) {
        JsonEntityMapper.Global.setDefault(m);
    }

    /**
     * 返回用于 JSON 序列化和反序列化的 {@link JsonEntityMapper}。
     *
     * @return {@link JsonEntityMapper} 实例
     */
    public static JsonEntityMapper getJsonEntityMapper() {
        return JsonEntityMapper.Global.getDefault();
    }

    /**
     * 将对象列表转换为指定目标类的对象列表。
     *
     * @param source 源对象列表
     * @param target 目标类
     * @param <T>    目标类的类型
     * @return 转换后的对象列表，如果源列表为null则返回空列表
     */
    public static <T> List<T> as(List<?> source, Class<T> target) {
        if (source == null) {
            return new ArrayList<>();
        }

        try {
            EntityMapper em = EntityMapper.Global.getDefault();
            List<T> ret = new ArrayList<T>();
            for (Object po : source) {
                T vo = target.getDeclaredConstructor().newInstance();
                em.assign(po, vo);
                ret.add(vo);
            }
            return ret;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /**
     * 将对象转换为指定目标类的对象。
     * 如果源对象为null，则返回目标类的空实例。
     *
     * @param source 源对象
     * @param target 目标类
     * @param <T>    目标类的类型
     * @return 转换后的对象
     */
    public static <T> T as(Object source, Class<T> target) {
        try {
            EntityMapper em = EntityMapper.Global.getDefault();
            T instance = target.getDeclaredConstructor().newInstance();

            if (source == null) {
                return instance;
            }

            em.assign(source, instance);
            return instance;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 将源对象的值赋给目标对象。
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void as(Object source, Object target) {
        EntityMapper.Global.getDefault().assign(source, target);
    }

    /**
     * 将源对象的值赋给目标对象。
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void assign(Object source, Object target) {
        EntityMapper.Global.getDefault().assign(source, target);
    }

    /**
     * 将 JSON 字符串反序列化为指定目标类的对象。
     * 如果反序列化失败，返回null。
     *
     * @param json   要反序列化的 JSON 字符串
     * @param target 目标类
     * @param <T>    目标类的类型
     * @return 反序列化后的对象，失败时返回null
     */
    public static <T> T fromJson(String json, Class<T> target) {
        try {
            return JsonEntityMapper.Global.getDefault().fromJson(json, target);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将 JSON 数组字符串反序列化为指定目标类的对象列表。
     * 如果JSON字符串为空或反序列化失败，返回空列表。
     *
     * @param json   要反序列化的 JSON 数组字符串
     * @param target 目标类
     * @param <T>    目标类的类型
     * @return 反序列化后的对象列表
     */
    public static <T> List<T> fromJsonArray(String json, Class<T> target) {
        if (Strings.isBlank(json)) {
            return new ArrayList<>();
        }

        try {
            return JsonEntityMapper.Global.getDefault().fromJsonArray(json, target);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 将对象序列化为 JSON 字符串。
     *
     * @param object 要序列化的对象
     * @return 对象的 JSON 字符串表示形式
     */
    public static String toJson(Object object) {
        return JsonEntityMapper.Global.getDefault().toJson(object);
    }

}
