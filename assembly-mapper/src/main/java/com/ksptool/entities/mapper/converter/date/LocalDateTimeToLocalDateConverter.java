package com.ksptool.entities.mapper.converter.date;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LocalDateTime 转 LocalDate 的类型转换器。
 */
public class LocalDateTimeToLocalDateConverter implements Converter<LocalDateTime, LocalDate> {

    /**
     * 将 LocalDateTime 转换为 LocalDate。
     *
     * @param context 映射上下文
     * @return 转换后的 LocalDate 对象，如果源为null则返回null
     */
    @Override
    public LocalDate convert(MappingContext<LocalDateTime, LocalDate> context) {
        if (context.getSource() == null) {
            return null;
        }
        return context.getSource().toLocalDate();
    }

}

