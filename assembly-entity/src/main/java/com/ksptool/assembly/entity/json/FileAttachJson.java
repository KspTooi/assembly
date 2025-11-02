package com.ksptool.assembly.entity.json;

import com.ksptool.entities.Entities;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class FileAttachJson {

    @NotBlank(message = "path不可为空")
    private String path;

    @NotBlank(message = "name不可为空")
    private String name;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 解析JSON字符串为FileAttachJson集合
     * 安全函数，如果解析失败，将会安全的返回空集合
     *
     * @param jsonString JSON字符串
     * @return FileAttachJson集合
     */
    public static List<FileAttachJson> fromJson(String jsonString) {
        return Entities.fromJsonArray(jsonString, FileAttachJson.class);
    }

    /**
     * 将FileAttachJson集合转换为JSON字符串
     *
     * @param list FileAttachJson集合
     * @return JSON字符串
     */
    public static String toJson(List<FileAttachJson> list) {
        return Entities.toJson(list);
    }
}
