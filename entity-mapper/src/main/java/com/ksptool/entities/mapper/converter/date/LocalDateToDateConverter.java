package com.ksptool.entities.mapper.converter.date;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * LocalDate 转 Date 的类型转换器。
 */
public class LocalDateToDateConverter implements Converter<LocalDate, Date> {

    /**
     * 将 LocalDate 转换为 Date。
     *
     * @param context 映射上下文
     * @return 转换后的 Date 对象，如果源为null则返回null
     */
    @Override
    public Date convert(MappingContext<LocalDate, Date> context) {
        if (context.getSource() == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = context.getSource().atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

}

