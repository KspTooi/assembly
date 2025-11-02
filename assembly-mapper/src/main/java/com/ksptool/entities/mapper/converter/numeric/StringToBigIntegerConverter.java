package com.ksptool.entities.mapper.converter.numeric;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.math.BigInteger;

/**
 * String 转 BigInteger 的类型转换器。
 */
public class StringToBigIntegerConverter implements Converter<String, BigInteger> {

    /**
     * 将 String 转换为 BigInteger。
     *
     * @param context 映射上下文
     * @return 转换后的 BigInteger 对象，如果源为null或解析失败则返回null，解析失败时抛出RuntimeException
     */
    @Override
    public BigInteger convert(MappingContext<String, BigInteger> context) {
        if (context.getSource() == null || context.getSource().trim().isEmpty()) {
            return null;
        }
        try {
            return new BigInteger(context.getSource().trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

}

