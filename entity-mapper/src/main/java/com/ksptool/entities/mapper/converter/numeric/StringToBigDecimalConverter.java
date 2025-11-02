package com.ksptool.entities.mapper.converter.numeric;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.math.BigDecimal;

/**
 * String 转 BigDecimal 的类型转换器。
 */
public class StringToBigDecimalConverter implements Converter<String, BigDecimal> {

    /**
     * 将 String 转换为 BigDecimal。
     *
     * @param context 映射上下文
     * @return 转换后的 BigDecimal 对象，如果源为null或解析失败则返回null，解析失败时抛出RuntimeException
     */
    @Override
    public BigDecimal convert(MappingContext<String, BigDecimal> context) {
        if (context.getSource() == null || context.getSource().trim().isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(context.getSource().trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

}

