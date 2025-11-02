package com.ksptool.entities.mapper.converter.date;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Timestamp 转 Date 的类型转换器。
 */
public class TimestampToDateConverter implements Converter<Timestamp, Date> {

    /**
     * 将 Timestamp 转换为 Date。
     *
     * @param context 映射上下文
     * @return 转换后的 Date 对象，如果源为null则返回null
     */
    @Override
    public Date convert(MappingContext<Timestamp, Date> context) {
        if (context.getSource() == null) {
            return null;
        }
        return new Date(context.getSource().getTime());
    }

}

