package com.ksptool.entities.mapper.converter.date;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * LocalDateTime 转 Date 的类型转换器。
 */
public class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {

    /**
     * 将 LocalDateTime 转换为 Date。
     *
     * @param context 映射上下文
     * @return 转换后的 Date 对象，如果源为null则返回null
     */
    @Override
    public Date convert(MappingContext<LocalDateTime, Date> context) {
        if (context.getSource() == null) {
            return null;
        }
        return Date.from(context.getSource().atZone(ZoneId.systemDefault()).toInstant());
    }

}

