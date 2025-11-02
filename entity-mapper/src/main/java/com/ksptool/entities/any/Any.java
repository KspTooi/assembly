package com.ksptool.entities.any;

import com.ksptool.entities.ObjectUtils;
import com.ksptool.entities.mapper.EntityMapper;
import com.ksptool.entities.mapper.JsonEntityMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 泛型容器类，用于存放 T 类型的对象，并提供用于操作和转换该对象的实用方法。
 *
 * @param <T> 容器所持有的对象的类型
 */
public class Any<T> {

    private final T content;
    private final String contentType;
    private final Map<String, Object> putMap = new ConcurrentHashMap<>();
    private final Map<String, Object> getMap = new ConcurrentHashMap<>();

    /**
     * 使用指定的内容构造 Any 对象。
     *
     * @param t 要由容器持有的对象
     */
    public Any(T t) {
        this.content = t;
        this.contentType = "object";
    }

    /**
     * 使用指定的非空内容创建 Any 对象。
     *
     * @param t   要由容器持有的非空对象
     * @param <T> 对象的类型
     * @return 创建的 Any 对象
     * @throws NullPointerException 如果提供的对象为空
     */
    public static <T> Any<T> of(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return new Any<T>(t);
    }

    /**
     * 使用指定的列表创建 Any 对象，如果列表为null则创建空列表的Any对象。
     *
     * @param list 要持有的列表，可以为null
     * @param <T>  列表元素类型
     * @return 创建的 Any 对象
     */
    public static <T> Any<List<T>> ofList(List<T> list) {
        if (list == null) {
            return new Any<List<T>>(new ArrayList<>());
        }
        return new Any<List<T>>(list);
    }

    /**
     * 将内容转换为Map类型。
     * 通过反射获取对象的所有字段和getter方法，将其转换为Map。
     * 如果对象本身就是Map类型，则直接返回。
     *
     * @param <K> Map的键类型
     * @param <V> Map的值类型
     * @return 转换后的Map对象，如果内容为null则返回空Map
     */
    public <K, V> Map<K, V> asMap() {
        if (content == null) {
            return new HashMap<>();
        }

        if (content instanceof Map) {
            return (Map<K, V>) content;
        }

        Map<K, V> resultMap = new HashMap<>();

        try {
            Class<?> clazz = content.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(content);
                if (fieldValue != null) {
                    resultMap.put((K) fieldName, (V) fieldValue);
                }
            }

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                String methodName = method.getName();

                if (methodName.startsWith("get") &&
                        method.getParameterCount() == 0 &&
                        !methodName.equals("getClass")) {

                    String propertyName = methodName.substring(3, 4).toLowerCase() +
                            (methodName.length() > 4 ? methodName.substring(4) : "");

                    if (!resultMap.containsKey(propertyName)) {
                        Object propertyValue = method.invoke(content);
                        if (propertyValue != null) {
                            resultMap.put((K) propertyName, (V) propertyValue);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Map<String, Object> tempMap = new HashMap<>();
            EntityMapper.Global.getDefault().assign(content, tempMap);
            resultMap.putAll((Map<? extends K, ? extends V>) tempMap);
        }

        return resultMap;
    }

    /**
     * 使用指定的内容（可以为空）创建 Any 对象。
     *
     * @param t   要由容器持有的对象，可以为null
     * @param <T> 对象的类型
     * @return 创建的 Any 对象
     */
    public static <T> Any<T> ofNullable(T t) {
        return new Any<>(t);
    }

    /**
     * 创建一个持有空 HashMap 的 Any 对象。
     *
     * @return 创建的 Any 对象
     */
    public static Any<Map<String, Object>> of() {
        return of(null);
    }

    /**
     * 获取容器持有的对象。
     *
     * @return 容器持有的对象
     */
    public T get() {
        return content;
    }

    /**
     * 将内容对象转换为指定的目标类。
     *
     * @param target 目标类，内容对象应转换为该类
     * @param <R>    目标类的类型
     * @return 转换后的对象
     */
    public <R> R as(Class<R> target) {
        try {
            EntityMapper em = EntityMapper.Global.getDefault();
            R instance = target.getDeclaredConstructor().newInstance();
            if (content == null) {
                return instance;
            }
            em.assign(content, instance);
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 将内容对象（应为 List）转换为指定目标类的 List。
     * 如果内容不是List类型，但可以转换为目标类型，则返回包含该对象的列表。
     *
     * @param target 目标类，列表的元素应转换为该类
     * @param <R>    目标类的类型
     * @return 转换后的 List，如果无法转换则返回空列表
     */
    public <R> List<R> asList(Class<R> target) {
        if (!(content instanceof List)) {
            if (target.isInstance(content)) {
                List<R> ret = new ArrayList<>();
                ret.add((R) content);
                return ret;
            }
            return Collections.emptyList();
        }

        List<?> sourceList = (List<?>) content;
        if (sourceList == null) {
            return new ArrayList<>();
        }

        try {
            EntityMapper em = EntityMapper.Global.getDefault();
            List<R> ret = new ArrayList<R>();
            for (Object po : sourceList) {
                R vo = target.getDeclaredConstructor().newInstance();
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
     * 将内容对象转换为指定的目标类，并将其包装在一个新的 Any 对象中。
     *
     * @param target   目标类，内容对象应转换为该类
     * @param <TARGET> 目标类的类型
     * @return 持有转换后对象的新 Any 对象
     */
    public <TARGET> Any<TARGET> to(Class<TARGET> target) {
        return Any.of(this.as(target));
    }

    /**
     * 将内容对象（应为 List）转换为指定目标类的 List，并将其包装在一个新的 Any 对象中。
     *
     * @param target   目标类，列表的元素应转换为该类
     * @param <TARGET> 目标类的类型
     * @return 持有转换后 List 的新 Any 对象
     */
    public <TARGET> Any<List<TARGET>> toList(Class<TARGET> target) {
        return Any.of(this.asList(target));
    }

    /**
     * 创建一个 AnyMatchUpdate 对象，用于对内容对象执行条件更新。
     *
     * @param updateField 要更新的字段
     * @return 创建的 AnyMatchUpdate 对象
     */
    public AnyMatchUpdate matchUpdate(String updateField) {
        return new AnyMatchUpdate(this, updateField);
    }

    /**
     * 创建一个 AnyMatchUpdate 对象，用于使用指定的匹配值对内容对象执行条件更新。
     *
     * @param updateField 要更新的字段
     * @param matchValue  要匹配的值
     * @return 创建的 AnyMatchUpdate 对象
     */
    public AnyMatchUpdate matchUpdate(String updateField, String matchValue) {
        return new AnyMatchUpdate(this, updateField, matchValue);
    }

    /**
     * 检查内容对象是否为空。
     *
     * @return 如果内容对象为空则返回 true，否则返回 false
     */
    public boolean isNull() {
        return this.content == null;
    }

    /**
     * 将指定对象的值赋给当前容器中的内容对象。
     * 如果对象为null，则不做任何操作。
     *
     * @param object 要赋值的源对象
     * @return 当前的 Any 对象，支持链式调用
     */
    public Any<T> assign(Object object) {
        if (object == null) {
            return this;
        }
        EntityMapper.Global.getDefault().assign(object, content);
        return this;
    }

    /**
     * 将指定的值赋给指定的键，并更新内容对象。
     * 如果内容对象是List，则更新列表中的每个元素。
     * 如果内容对象是Map，则直接更新Map。
     * 其他情况则通过EntityMapper进行赋值。
     *
     * @param k 要分配值的键
     * @param v 要分配的值
     * @return 当前的 Any 对象，支持链式调用
     */
    public Any<T> val(String k, Object v) {
        try {
            EntityMapper em = EntityMapper.Global.getDefault();
            putMap.put(k, v);

            if (content instanceof List) {
                for (Object item : (List<?>) content) {
                    em.assign(putMap, item);
                }
                return this;
            }

            if (content instanceof Map) {
                ((Map<String, Object>) content).put(k, v);
                return this;
            }

            em.assign(putMap, content);
        } finally {
            putMap.clear();
        }

        return this;
    }

    /**
     * 获取指定字段的值，返回字符串形式。
     *
     * @param field 字段名
     * @return 字段值的字符串表示
     */
    public String val(String field) {
        return ObjectUtils.getPropertyValue(content, field).toString();
    }

    /**
     * 获取指定字段的值，并转换为指定类型。
     *
     * @param field  字段名
     * @param tClass 目标类型
     * @param <RETURN> 返回值类型
     * @return 字段值，如果为null或类型不匹配则返回null
     */
    public <RETURN> RETURN val(String field, Class<RETURN> tClass) {
        putMap.clear();
        EntityMapper.Global.getDefault().assign(content, putMap);
        Object value = putMap.get(field);
        if (value == null) {
            return null;
        }
        if (tClass.isInstance(value)) {
            return (RETURN) value;
        }
        return null;
    }

    /**
     * 使用函数式接口获取指定字段的值。
     *
     * @param getter 获取值的函数
     * @param <R>    返回值类型
     * @return 函数执行结果
     */
    public <R> R val(Function<T, R> getter) {
        return getter.apply(content);
    }

    /**
     * 使用BiConsumer设置值，支持链式调用。
     *
     * @param bi 设置值的BiConsumer函数
     * @param i  要设置的值
     * @param <I> 值的类型
     * @return 当前的 Any 对象，支持链式调用
     */
    public <I> Any<T> val(BiConsumer<T, I> bi, I i) {
        bi.accept(content, i);
        return this;
    }

}
