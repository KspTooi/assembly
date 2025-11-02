package com.ksptool.entities.mapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * String 转 Date 的类型转换器。
 * 使用格式 "yyyy-MM-dd HH:mm:ss" 进行日期解析。
 */
public class StringToDateConverter implements Converter<String, Date> {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 将 String 转换为 Date。
     *
     * @param context 映射上下文
     * @return 转换后的日期对象，如果源为null或解析失败则返回null，解析失败时抛出RuntimeException
     */
    @Override
    public Date convert(MappingContext<String, Date> context) {
        if (context.getSource() == null) {
            return null;
        }
        try {
            return SDF.parse(context.getSource());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}

