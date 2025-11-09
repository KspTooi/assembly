package com.ksptool.assembly.blueprint.core;


import com.ksptool.assembly.blueprint.collector.BlueprintCollector;
import com.ksptool.assembly.blueprint.collector.RawCollector;
import com.ksptool.assembly.blueprint.converter.PolyConverter;
import com.ksptool.assembly.blueprint.entity.blueprint.RawBlueprint;
import com.ksptool.assembly.blueprint.entity.field.PolyTable;
import com.ksptool.assembly.blueprint.entity.field.RawTable;
import com.ksptool.assembly.blueprint.projector.Projector;
import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Getter
public class AssemblyFactory {

    /**
     * 数据采集器
     */
    private RawCollector rawCollector;

    /**
     * 蓝图采集器
     */
    private BlueprintCollector blueprintCollector;

    /**
     * 数据转换器
     */
    private PolyConverter converter;
    
    /**
     * 投影器
     */
    @Setter
    private Projector projector;


    /**
     * 基准路径
     */
    @Setter
    private String basePath;


    /**
     * 执行蓝图生成
     *
     * @return 多态表列表
     */
    public List<PolyTable> execute() {

        // 确保配置正确
        ensureConfiguration();

        // 收集数据
        List<RawTable> rawTables = rawCollector.collect();

        if (rawTables == null || rawTables.isEmpty()) {
            throw new RuntimeException("未能从数据源中收集到任何数据!");
        }
        
        // 收集蓝图
        List<RawBlueprint> blueprints = null;

        try {
            blueprints = blueprintCollector.collect(basePath);
        } catch (IOException e) {
            throw new RuntimeException("无法执行蓝图生成，因为无法收集蓝图：" + e.getMessage(), e);
        }

        if (blueprints == null || blueprints.isEmpty()) {
            throw new RuntimeException("未能从基准路径" + basePath + "中收集到任何蓝图!");
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


    /**
     * 设置数据采集器
     * @param rawCollector 数据采集器
     */
    public void setCollector(RawCollector rawCollector) {
        this.rawCollector = rawCollector;
    }

    /**
     * 设置蓝图采集器
     * @param blueprintCollector 蓝图采集器
     */
    public void setCollector(BlueprintCollector blueprintCollector) {
        this.blueprintCollector = blueprintCollector;
    }

    
    /**
     * 确保配置正确
     */
    public void ensureConfiguration() {
        if (rawCollector == null) {
            throw new RuntimeException("无法执行蓝图生成，因为数据采集器未配置!");
        }
        if (blueprintCollector == null) {
            throw new RuntimeException("无法执行蓝图生成，因为蓝图采集器未配置!");
        }
        if (converter == null) {
            throw new RuntimeException("无法执行蓝图生成，因为数据转换器未配置!");
        }
        if (projector == null) {
            throw new RuntimeException("无法执行蓝图生成，因为投影器未配置!");
        }
        validateBasePath();
    }

    /**
     * 校验基准路径
     */
    private void validateBasePath() {
        if (StringUtils.isBlank(basePath)) {
            throw new RuntimeException("无法执行蓝图生成，因为基准路径未配置!");
        }

        Path path = Paths.get(basePath);
        if (!Files.exists(path)) {
            throw new RuntimeException("无法执行蓝图生成，因为基准路径不存在：" + basePath);
        }

        if (!Files.isDirectory(path)) {
            throw new RuntimeException("无法执行蓝图生成，因为基准路径不是目录：" + basePath);
        }

        try {
            boolean hasContent;
            try (var stream = Files.list(path)) {
                hasContent = stream.findAny().isPresent();
            }
            if (!hasContent) {
                throw new RuntimeException("无法执行蓝图生成，因为基准路径目录为空：" + basePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("无法执行蓝图生成，因为无法读取基准路径目录：" + basePath, e);
        }
    }

}
