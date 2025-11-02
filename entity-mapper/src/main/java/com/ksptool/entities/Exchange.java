package com.ksptool.entities;

/**
 * 通用类型转换类，支持链式调用
 *
 * @param <S> 源类型
 * @param <T> 目标类型
 */
public class Exchange<S, T> {

    private S source;

    private T target;

    // 私有构造函数，通过静态方法创建实例
    private Exchange(S source, T target) {
        this.source = source;
        this.target = target;
    }


}
