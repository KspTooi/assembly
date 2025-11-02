package com.ksptool.entities.mapper.converter.numeric;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

/**
 * String 转 Long 的类型转换器。
 */
public class StringToLongConverter implements Converter<String, Long> {

    /**
     * 将 String 转换为 Long。
     *
     * @param context 映射上下文
     * @return 转换后的 Long 对象，如果源为null或解析失败则返回null，解析失败时抛出RuntimeException
     */
    @Override
    public Long convert(MappingContext<String, Long> context) {
        if (context.getSource() == null || context.getSource().trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(context.getSource().trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

}

