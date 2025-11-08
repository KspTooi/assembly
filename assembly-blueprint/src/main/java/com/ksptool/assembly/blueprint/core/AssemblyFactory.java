package com.ksptool.assembly.blueprint.core;


import com.ksptool.assembly.blueprint.collector.RawCollector;
import com.ksptool.assembly.blueprint.converter.PolyConverter;
import com.ksptool.assembly.blueprint.entity.field.PolyTable;
import com.ksptool.assembly.blueprint.entity.field.RawTable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class AssemblyFactory {

    /**
     * 数据收集器
     */
    private RawCollector collector;

    /**
     * 数据转换器
     */
    private PolyConverter converter;


    /**
     * 执行蓝图生成
     *
     * @return 多态表列表
     */
    public List<PolyTable> execute() {
        // 收集数据
        List<RawTable> rawTables = collector.collect();

        if (rawTables == null || rawTables.isEmpty()) {
            throw new RuntimeException("未能从数据源中收集到任何数据!");
        }

        // 转换批数据
        List<PolyTable> polyTables = new ArrayList<>();

        for (RawTable rawTable : rawTables) {
            PolyTable polyTable = converter.toPolyTable(rawTable);
            if (polyTable == null) {
                throw new RuntimeException("未能将数据转换为多态表!");
            }
            polyTables.add(polyTable);
        }

        return polyTables;
    }


}
