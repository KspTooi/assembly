package com.ksptool.assembly.blueprint.entity.field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RawField {

    //字段名称
    private String name;

    //字段类型
    private String type;

    //字段注释
    private String comment;

    //必填
    private boolean required;

    //是否主键
    private boolean primaryKey;

    //排序号
    private int seq;

}
