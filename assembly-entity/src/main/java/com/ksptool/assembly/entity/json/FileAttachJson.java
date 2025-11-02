package com.ksptool.assembly.entity.json;

import jakarta.validation.constraints.NotBlank;

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
}
