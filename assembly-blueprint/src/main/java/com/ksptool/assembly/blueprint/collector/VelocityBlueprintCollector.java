package com.ksptool.assembly.blueprint.collector;

import com.ksptool.assembly.blueprint.entity.blueprint.RawBlueprint;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Velocity蓝图收集器，用于收集Velocity蓝图
 * 蓝图文件名规则: blueprint.java.vm
 * 其中blueprint.java为蓝图的输出扩展名
 */
public class VelocityBlueprintCollector implements BlueprintCollector {

    private static final Logger logger = LoggerFactory.getLogger(VelocityBlueprintCollector.class);

    private String blueprintExtension = "vm";

    public void setBlueprintExtension(String blueprintExtension) {
        this.blueprintExtension = blueprintExtension;
    }

    @Override
    public List<RawBlueprint> collect(String basePath) throws IOException {
        if (StringUtils.isBlank(basePath)) {
            logger.warn("基准路径为空，无法收集蓝图");
            return new ArrayList<>();
        }

        Path base = Paths.get(basePath);
        if (!Files.exists(base)) {
            logger.warn("基准路径不存在：{}", basePath);
            return new ArrayList<>();
        }
        if (!Files.isDirectory(base)) {
            logger.warn("基准路径不是目录：{}", basePath);
            return new ArrayList<>();
        }

        logger.info("开始收集Velocity蓝图，基准路径：{}，扩展名：{}", basePath, blueprintExtension);
        List<RawBlueprint> blueprints = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(base)) {
            paths.filter(Files::isRegularFile)
                    .filter(this::matchesExtension)
                    .forEach(path -> {
                        RawBlueprint blueprint = createRawBlueprint(path, base);
                        if (blueprint != null) {
                            blueprints.add(blueprint);
                        }
                    });
        }

        logger.info("收集完成，共收集{}个蓝图文件", blueprints.size());
        return blueprints;
    }

    private boolean matchesExtension(Path path) {
        String fileName = path.getFileName().toString();
        if (StringUtils.isBlank(fileName)) {
            return false;
        }
        String extension = getExtension(fileName);
        return blueprintExtension.equalsIgnoreCase(extension);
    }

    private String getExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex < 0 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }

    private RawBlueprint createRawBlueprint(Path filePath, Path basePath) {
        try {
            String fileName = filePath.getFileName().toString();
            String absoluteFilePath = filePath.toAbsolutePath().toString();
            String relativeFilePath = basePath.relativize(filePath).toString().replace('\\', '/');
            String basePathStr = basePath.toAbsolutePath().toString();

            RawBlueprint blueprint = new RawBlueprint();
            blueprint.setFileName(fileName);
            blueprint.setAbsoluteFilePath(absoluteFilePath);
            blueprint.setRelativeFilePath(relativeFilePath);
            blueprint.setBasePath(basePathStr);

            logger.debug("收集蓝图文件：{}", relativeFilePath);
            return blueprint;
        } catch (Exception e) {
            logger.error("创建RawBlueprint失败，文件：{}", filePath, e);
            return null;
        }
    }

}
