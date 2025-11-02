package com.ksptool.entities.mapper;

import java.util.Map;

/**
 * 实体映射器接口，提供对象属性拷贝和映射的功能。
 */
public interface EntityMapper {

    /**
     * 可替换的全局默认实例，使用volatile保证可见性。
     * 采用延迟初始化，首次访问时创建。
     */
    class Global {
        private static volatile EntityMapper instance;

        /**
         * 获取全局默认实例，采用延迟初始化。
         *
         * @return 全局默认实例
         */
        public static EntityMapper getDefault() {
            if (instance == null) {
                synchronized (Global.class) {
                    if (instance == null) {
                        instance = new DefaultEntityMapper();
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
        public static synchronized void setDefault(EntityMapper mapper) {
            if (mapper == null) {
                throw new NullPointerException("EntityMapper不能为null");
            }
            instance = mapper;
        }

        /**
         * 重置为默认实现。
         */
        public static synchronized void reset() {
            instance = new DefaultEntityMapper();
        }
    }

    /**
     * 将源对象的值赋给目标对象。
     * 实现类需要根据属性名称和类型进行智能匹配和转换。
     *
     * @param source 源对象
     * @param target 目标对象
     */
    void assign(Object source, Object target);

    /**
     * 将源对象的值赋给目标对象，并使用指定的映射关系。
     * 映射关系定义了源属性名到目标属性名的对应关系。
     *
     * @param source 源对象
     * @param target 目标对象
     * @param map    属性映射关系，key为源属性名，value为目标属性名
     */
    void assign(Object source, Object target, Map<String, String> map);

}
