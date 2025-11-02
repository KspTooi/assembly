package com.ksptool.entities.mapper.converter.numeric;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

/**
 * Float 转 String 的类型转换器。
 */
public class FloatToStringConverter implements Converter<Float, String> {

    /**
     * 将 Float 转换为 String。
     *
     * @param context 映射上下文
     * @return 转换后的字符串，如果源为null则返回null
     */
    @Override
    public String convert(MappingContext<Float, String> context) {
        return context.getSource() != null ? context.getSource().toString() : null;
    }

}

