package com.ksptool.assembly.blueprint.entity.field;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RawTable {

    //表名称
    private String name;

    //表注释
    private String comment;

    //字段列表
    private List<RawField> fields;

    //排序号
    private int seq;

}
