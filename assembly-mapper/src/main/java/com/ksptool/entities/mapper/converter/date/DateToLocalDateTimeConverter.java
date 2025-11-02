package com.ksptool.entities.mapper.converter.date;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Date 转 LocalDateTime 的类型转换器。
 */
public class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

    /**
     * 将 Date 转换为 LocalDateTime。
     *
     * @param context 映射上下文
     * @return 转换后的 LocalDateTime 对象，如果源为null则返回null
     */
    @Override
    public LocalDateTime convert(MappingContext<Date, LocalDateTime> context) {
        if (context.getSource() == null) {
            return null;
        }
        return LocalDateTime.ofInstant(
                context.getSource().toInstant(),
                ZoneId.systemDefault()
        );
    }

}

