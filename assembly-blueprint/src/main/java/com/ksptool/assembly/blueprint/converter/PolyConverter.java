package com.ksptool.assembly.blueprint.converter;

import com.ksptool.assembly.blueprint.entity.field.PolyField;
import com.ksptool.assembly.blueprint.entity.field.PolyTable;
import com.ksptool.assembly.blueprint.entity.field.RawField;
import com.ksptool.assembly.blueprint.entity.field.RawTable;

public interface PolyConverter {

    /**
     * 将原始表转换为多态表
     *
     * @param rawTable 原始表
     * @return 多态表
     */
    public PolyTable toPolyTable(RawTable rawTable);


    /**
     * 将原始字段转换为多态字段
     *
     * @param rawField 原始字段
     * @return 多态字段
     */
    public PolyField toPolyField(RawField rawField);

}
