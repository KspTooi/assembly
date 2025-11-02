package com.ksptool.entities.mapper;

import java.util.List;

/**
 * JSON 实体映射器接口，提供对象与JSON字符串之间的序列化和反序列化功能。
 */
public interface JsonEntityMapper {

    /**
     * 可替换的全局默认实例，使用volatile保证可见性。
     * 采用延迟初始化，首次访问时创建。
     */
    class Global {
        private static volatile JsonEntityMapper instance;

        /**
         * 获取全局默认实例，采用延迟初始化。
         *
         * @return 全局默认实例
         */
        public static JsonEntityMapper getDefault() {
            if (instance == null) {
                synchronized (Global.class) {
                    if (instance == null) {
                        instance = new DefaultJsonEntityMapper();
                    }
                }
            }
            return instance;
        }

        /**
         * 设置全局默认实例，允许运行时替换。
         *
         * @param mapper 新的全局实例，不能为null
         * @throws NullPointerException 当mapper为null时抛出
         */
        public static synchronized void setDefault(JsonEntityMapper mapper) {
            if (mapper == null) {
                throw new NullPointerException("JsonEntityMapper不能为null");
            }
            instance = mapper;
        }

        /**
         * 重置为默认实现。
         */
        public static synchronized void reset() {
            instance = new DefaultJsonEntityMapper();
        }
    }

    /**
     * 将对象转换为 JSON 字符串。
     *
     * @param obj 要转换的对象
     * @return 对象的 JSON 字符串表示形式
     */
    String toJson(Object obj);

    /**
     * 将 JSON 字符串转换为指定类的对象。
     *
     * @param json  要转换的 JSON 字符串
     * @param clazz 要创建的对象的类
     * @param <T>   要创建的对象的类型
     * @return 从 JSON 字符串创建的对象
     */
    <T> T fromJson(String json, Class<T> clazz);

    /**
     * 将 JSON 数组字符串转换为指定类的对象列表。
     *
     * @param json  要转换的 JSON 数组字符串
     * @param clazz 列表中对象的类
     * @param <T>   列表中对象的类型
     * @return 从 JSON 数组字符串创建的对象列表
     */
    <T> List<T> fromJsonArray(String json, Class<T> clazz);
}
