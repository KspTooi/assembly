package com.ksptool.assembly.blueprint.utils;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathTool {

    /**
     * 获取当前工作目录
     * @return 当前工作目录
     */
    public static String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取当前工作目录与指定路径的组合路径
     * @param path 相对路径或绝对路径
     * @return 组合后的路径字符串
     */
    public static String getCurrentDirectory(String path) {
        //如果路径为空，则返回当前工作目录
        if (StringUtils.isBlank(path)) {
            return getCurrentDirectory();
        }
        //如果路径为绝对路径，则直接返回
        Path currentDir = Paths.get(getCurrentDirectory());

        //如果路径为相对路径，则转换为绝对路径
        Path targetPath = Paths.get(path);
        if (targetPath.isAbsolute()) {
            return targetPath.normalize().toString();
        }
        return currentDir.resolve(targetPath).normalize().toString();
    }

}
