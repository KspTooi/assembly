package com.ksptool.entities.mapper.converter.date;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * String 转 LocalDateTime 的类型转换器。
 * 使用格式 "yyyy-MM-dd HH:mm:ss" 进行日期解析。
 * 使用 DateTimeFormatter 保证线程安全，无需创建多个实例。
 */
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 将 String 转换为 LocalDateTime。
     *
     * @param context 映射上下文
     * @return 转换后的 LocalDateTime 对象，如果源为null或解析失败则返回null，解析失败时抛出RuntimeException
     */
    @Override
    public LocalDateTime convert(MappingContext<String, LocalDateTime> context) {
        if (context.getSource() == null) {
            return null;
        }
        try {
            return LocalDateTime.parse(context.getSource(), FORMATTER);
        } catch (DateTimeParseException e) {
            throw new RuntimeException(e);
        }
    }

}

