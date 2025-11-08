package com.ksptool.assembly.blueprint.collector;

import com.ksptool.assembly.blueprint.entity.field.RawTable;

import java.util.List;

public interface RawCollector {

    /**
     * 收集原始表
     *
     * @return 原始表列表
     */
    public List<RawTable> collect();

}
