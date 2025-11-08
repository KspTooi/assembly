package com.ksptool.assembly.blueprint.projector;

import com.ksptool.assembly.blueprint.entity.field.PolyTable;

import java.util.Map;

/*
 * 投影器接口，用于将多态表投影为代码文件
 */
public interface Projector {

    /**
     * 投影多态表为代码文件
     *
     * @param poly   多态表
     * @param params 额外提供给投影器的参数
     * @return 投影后的代码文件数量
     */
    int project(PolyTable poly, Map<String, Object> params);

}
