package com.ksptool.assembly.blueprint.converter;

import com.ksptool.assembly.blueprint.entity.blueprint.PolyBlueprint;
import com.ksptool.assembly.blueprint.entity.blueprint.RawBlueprint;
import com.ksptool.assembly.blueprint.entity.field.RawTable;

/**
 * 蓝图转换器接口
 */
public interface BlueprintConverter {


    /**
     * 将原始蓝图转换为多态蓝图
     *
     * @param rawBlueprint 原始蓝图
     * @return 多态蓝图
     */
    public PolyBlueprint toPolyBlueprint(RawBlueprint rawBlueprint, RawTable rawTable);

}
