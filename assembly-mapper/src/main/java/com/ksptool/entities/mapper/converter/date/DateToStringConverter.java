package com.ksptool.entities.mapper.converter.date;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Date 转 String 的类型转换器。
 * 使用格式 "yyyy-MM-dd HH:mm:ss" 进行日期格式化。
 * 使用 DateTimeFormatter 保证线程安全，无需创建多个实例。
 */
public class DateToStringConverter implements Converter<Date, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 将 Date 转换为 String。
     *
     * @param context 映射上下文
     * @return 转换后的字符串，如果源为null则返回null
     */
    @Override
    public String convert(MappingContext<Date, String> context) {
        if (context.getSource() == null) {
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.ofInstant(
                context.getSource().toInstant(),
                ZoneId.systemDefault()
        );
        return localDateTime.format(FORMATTER);
    }

}

