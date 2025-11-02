package com.ksptool.entities.mapper.converter.numeric;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

/**
 * String 转 Double 的类型转换器。
 */
public class StringToDoubleConverter implements Converter<String, Double> {

    /**
     * 将 String 转换为 Double。
     *
     * @param context 映射上下文
     * @return 转换后的 Double 对象，如果源为null或解析失败则返回null，解析失败时抛出RuntimeException
     */
    @Override
    public Double convert(MappingContext<String, Double> context) {
        if (context.getSource() == null || context.getSource().trim().isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(context.getSource().trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

}

