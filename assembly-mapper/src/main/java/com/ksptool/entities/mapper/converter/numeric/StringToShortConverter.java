package com.ksptool.entities.mapper.converter.numeric;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

/**
 * String 转 Short 的类型转换器。
 */
public class StringToShortConverter implements Converter<String, Short> {

    /**
     * 将 String 转换为 Short。
     *
     * @param context 映射上下文
     * @return 转换后的 Short 对象，如果源为null或解析失败则返回null，解析失败时抛出RuntimeException
     */
    @Override
    public Short convert(MappingContext<String, Short> context) {
        if (context.getSource() == null || context.getSource().trim().isEmpty()) {
            return null;
        }
        try {
            return Short.parseShort(context.getSource().trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

}

