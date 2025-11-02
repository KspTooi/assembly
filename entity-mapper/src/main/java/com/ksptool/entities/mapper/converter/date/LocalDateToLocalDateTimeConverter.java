package com.ksptool.entities.mapper.converter.date;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LocalDate 转 LocalDateTime 的类型转换器。
 * 时间部分默认设置为 00:00:00。
 */
public class LocalDateToLocalDateTimeConverter implements Converter<LocalDate, LocalDateTime> {

    /**
     * 将 LocalDate 转换为 LocalDateTime。
     * 时间部分设置为当天的开始时间（00:00:00）。
     *
     * @param context 映射上下文
     * @return 转换后的 LocalDateTime 对象，如果源为null则返回null
     */
    @Override
    public LocalDateTime convert(MappingContext<LocalDate, LocalDateTime> context) {
        if (context.getSource() == null) {
            return null;
        }
        return context.getSource().atStartOfDay();
    }

}

