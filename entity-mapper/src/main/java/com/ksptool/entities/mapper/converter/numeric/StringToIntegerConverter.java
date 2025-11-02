package com.ksptool.entities.mapper.converter.numeric;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

/**
 * String 转 Integer 的类型转换器。
 */
public class StringToIntegerConverter implements Converter<String, Integer> {

    /**
     * 将 String 转换为 Integer。
     *
     * @param context 映射上下文
     * @return 转换后的 Integer 对象，如果源为null或解析失败则返回null，解析失败时抛出RuntimeException
     */
    @Override
    public Integer convert(MappingContext<String, Integer> context) {
        if (context.getSource() == null || context.getSource().trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(context.getSource().trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

}

