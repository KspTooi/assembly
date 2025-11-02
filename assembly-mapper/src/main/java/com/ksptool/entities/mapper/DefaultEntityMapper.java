package com.ksptool.entities.mapper;

import com.ksptool.entities.mapper.converter.date.DateToStringConverter;
import com.ksptool.entities.mapper.converter.date.StringToDateConverter;
import com.ksptool.entities.mapper.converter.numeric.IntegerToStringConverter;
import com.ksptool.entities.mapper.converter.numeric.LongToStringConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Map;

/**
 * 默认的实体映射器实现。
 * 使用ModelMapper进行对象属性的拷贝，并自定义了一些类型转换器。
 */
public class DefaultEntityMapper implements EntityMapper {

    private static final ModelMapper mMapper = new ModelMapper();

    /**
     * 构造函数，初始化ModelMapper的配置。
     */
    public DefaultEntityMapper() {
        init();
    }

    /**
     * 初始化ModelMapper，添加自定义的类型转换器并设置匹配策略。
     * 包括：Integer转String、Long转String、String转Date、Date转String的转换器。
     */
    public void init() {
        mMapper.addConverter(new IntegerToStringConverter());
        mMapper.addConverter(new LongToStringConverter());
        mMapper.addConverter(new StringToDateConverter());
        mMapper.addConverter(new DateToStringConverter());
        mMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    /**
     * 将源对象的值赋给目标对象。
     * 使用ModelMapper进行严格的属性匹配和类型转换。
     *
     * @param source 源对象
     * @param target 目标对象
     */
    @Override
    public void assign(Object source, Object target) {
        mMapper.map(source, target);
    }

    /**
     * 将源对象的值赋给目标对象，并使用指定的映射关系。
     * 注意：当前实现为空，需要子类或后续实现。
     *
     * @param source 源对象
     * @param target 目标对象
     * @param map    属性映射关系
     */
    @Override
    public void assign(Object source, Object target, Map<String, String> map) {
        // TODO: 实现自定义映射逻辑
    }


    /**
     * 动态注册类型转换器。
     * 允许在运行时添加自定义的类型转换逻辑。
     *
     * @param converter 要注册的转换器
     * @param <S>       源类型
     * @param <D>       目标类型
     */
    public <S, D> void addConverter(Converter<S, D> converter) {
        if (converter == null) {
            return;
        }
        mMapper.addConverter(converter);
    }

    /**
     * 获取内部的ModelMapper实例，用于高级配置和操作。
     *
     * @return ModelMapper实例
     */
    public ModelMapper getModelMapper() {
        return mMapper;
    }

}
