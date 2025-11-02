package com.ksptool.assembly.entity.json;

import jakarta.validation.constraints.NotBlank;

public class FileAttachJson {

    @NotBlank(message = "path不可为空")
    private String path;

    @NotBlank(message = "name不可为空")
    private String name;

}
