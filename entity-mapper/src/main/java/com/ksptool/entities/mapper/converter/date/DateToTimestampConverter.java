package com.ksptool.entities.mapper.converter.date;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Date 转 Timestamp 的类型转换器。
 */
public class DateToTimestampConverter implements Converter<Date, Timestamp> {

    /**
     * 将 Date 转换为 Timestamp。
     *
     * @param context 映射上下文
     * @return 转换后的 Timestamp 对象，如果源为null则返回null
     */
    @Override
    public Timestamp convert(MappingContext<Date, Timestamp> context) {
        if (context.getSource() == null) {
            return null;
        }
        return new Timestamp(context.getSource().getTime());
    }

}

