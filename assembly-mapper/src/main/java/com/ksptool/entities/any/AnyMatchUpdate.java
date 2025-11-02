package com.ksptool.entities.any;

import com.ksptool.entities.Entities;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供用于对 Any 对象的内容执行条件更新的 fluent API。
 * 此类允许您指定要更新的字段以及要匹配的值或字段。
 * 如果满足匹配条件，则将指定的更新应用于内容对象。
 */
public class AnyMatchUpdate {

    private final Any<?> any;

    private final String updateField;

    private String matchValue;

    /**
     * 使用指定的 Any 对象和更新字段构造 AnyMatchUpdate 对象。
     *
     * @param any         其内容应有条件更新的 Any 对象
     * @param updateField 要更新的字段
     */
    public AnyMatchUpdate(Any<?> any, String updateField) {
        this.any = any;
        this.updateField = updateField;
    }

    /**
     * 使用指定的 Any 对象、更新字段和匹配值构造 AnyMatchUpdate 对象。
     *
     * @param any         其内容应有条件更新的 Any 对象
     * @param updateField 要更新的字段
     * @param matchValue  要匹配的值
     */
    public AnyMatchUpdate(Any<?> any, String updateField, String matchValue) {
        this.any = any;
        this.updateField = updateField;
        this.matchValue = matchValue;
    }

    /**
     * 检查指定的值或字段是否等于匹配值或内容对象中更新字段的值。
     * 如果满足条件，则将指定的值赋给内容对象中的更新字段。
     * 此方法支持 List 和 Map 内容类型。
     *
     * @param valOrField 要比较的值或字段
     * @param append     如果满足条件，要赋给更新字段的值
     * @return 当前的 AnyMatchUpdate 对象，用于方法链式调用
     */
    public AnyMatchUpdate eq(String valOrField, Object append) {
        if (matchValue != null) {
            if (any.get() instanceof List) {
                for (Object item : (List<?>) any.get()) {
                    if (valOrField.equals(matchValue)) {
                        Map<String, Object> assignMap = new HashMap<>();
                        assignMap.put(updateField, append);
                        Entities.assign(assignMap, item);
                    }
                }
            }

            if (valOrField.equals(matchValue)) {
                any.val(updateField, append);
            }
        }

        if (matchValue == null) {
            if (any.get() instanceof List) {
                for (Object item : (List<?>) any.get()) {
                    String fieldVal = getFieldAsString(item, updateField);

                    if (fieldVal == null) {
                        return this;
                    }

                    if (fieldVal.equals(valOrField)) {
                        any.val(updateField, append);
                    }
                }
            }

            if (any.get() instanceof Map) {
                Object mapVal = ((Map<?, ?>) any.get()).get(updateField);
                if (mapVal == null) {
                    return this;
                }
                if (mapVal.toString().equals(valOrField)) {
                    any.val(updateField, append);
                }
            }

            String fieldVal = getFieldAsString(updateField);

            if (fieldVal == null) {
                return this;
            }

            if (fieldVal.equals(valOrField)) {
                any.val(updateField, append);
            }
            return this;
        }

        return this;
    }

    /**
     * 返回与此 AnyMatchUpdate 对象关联的 Any 对象。
     *
     * @return Any 对象
     */
    public Any<?> fin() {
        return this.any;
    }

    /**
     * 获取指定字段的字符串值。
     *
     * @param fieldName 字段名
     * @return 字段值的字符串表示，获取失败返回null
     */
    private String getFieldAsString(String fieldName) {
        return getFieldAsString(this.any.get(), fieldName);
    }

    /**
     * 获取对象指定字段的字符串值。
     *
     * @param item      对象实例
     * @param fieldName 字段名
     * @return 字段值的字符串表示，获取失败返回null
     */
    private String getFieldAsString(Object item, String fieldName) {
        try {
            Field field = item.getClass().getDeclaredField(updateField);
            field.setAccessible(true);
            return field.get(item).toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将关联的 Any 对象的内容对象转换为指定的目标类。
     *
     * @param target 目标类，内容对象应转换为该类
     * @param <R>    目标类的类型
     * @return 转换后的对象
     */
    public <R> R as(Class<R> target) {
        return any.as(target);
    }

    /**
     * 将关联的 Any 对象的内容对象（应为 List）转换为指定目标类的 List。
     *
     * @param target 目标类，列表的元素应转换为该类
     * @param <R>    目标类的类型
     * @return 转换后的 List
     */
    public <R> List<R> asList(Class<R> target) {
        return any.asList(target);
    }

}
