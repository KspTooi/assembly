package com.ksptool.entities.mapper.converter.numeric;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

/**
 * String 转 Byte 的类型转换器。
 */
public class StringToByteConverter implements Converter<String, Byte> {

    /**
     * 将 String 转换为 Byte。
     *
     * @param context 映射上下文
     * @return 转换后的 Byte 对象，如果源为null或解析失败则返回null，解析失败时抛出RuntimeException
     */
    @Override
    public Byte convert(MappingContext<String, Byte> context) {
        if (context.getSource() == null || context.getSource().trim().isEmpty()) {
            return null;
        }
        try {
            return Byte.parseByte(context.getSource().trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

}

