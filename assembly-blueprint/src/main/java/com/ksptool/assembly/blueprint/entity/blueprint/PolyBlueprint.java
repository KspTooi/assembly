package com.ksptool.assembly.blueprint.entity.blueprint;


import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class PolyBlueprint {

    //原始蓝图
    private RawBlueprint rawBlueprint;

    //最终输出文件名
    private String outputFileName;

    //最终输出路径(包含文件名)
    private String outputFilePath;

    /**
     * 获取最终输出文件路径
     * @return 最终输出文件路径
     */
    public Path getOutputFilePath() {
        return Paths.get(outputFilePath);
    }


}
