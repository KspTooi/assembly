package com.ksptool.assembly.blueprint.entity.field;

import com.ksptool.assembly.blueprint.utils.StdName;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 聚合表实体类
 * 用于存储聚合表的元数据
 * 包括表的名称、字段列表、排序号、导入列表等
 * 用于生成聚合表的代码
 * 用于生成聚合表的SQL
 * 用于生成聚合表的XML
 * 用于生成聚合表的JSON
 * 用于生成聚合表的YAML
 * 用于生成聚合表的PROPERTIES
 */
@Getter
public class PolyTable {

    //对应原始表
    @Setter
    private RawTable rawTable;

    //表标准化名称(ptstn) ex: LocalUserAccount
    private String stdName;

    //用于简写表名称
    private String ptstn; //表标准化名称(ptstn) ex: LocalUserAccount
    private String ptscn; //表小驼峰名称(ptscn) ex: localUserAccount
    private String ptbcn; //表大驼峰名称(ptbcn) ex: LocalUserAccount
    private String ptuln; //表下划线名称(ptuln) ex: local_user_account
    private String ptalcn; //表全小写名称(ptalcn) ex: localuseraccount
    private String ptaucn; //表全大写名称(ptaucn) ex: LOCALUSERACCOUNT

    //表注释
    @Setter
    private String comment;

    //字段列表
    @Setter
    private List<PolyField> fields;

    //排序号
    @Setter
    private int seq;

    //导入列表
    @Setter
    private List<PolyImport> imports;

    public void setStdName(String stdName) {
        if (StringUtils.isBlank(stdName)) {
            this.stdName = stdName;
            this.ptstn = stdName;
            this.ptscn = "";
            this.ptbcn = "";
            this.ptuln = "";
            this.ptalcn = "";
            this.ptaucn = "";
            return;
        }

        StdName stdNameObj = StdName.of(stdName);
        this.stdName = stdNameObj.getValue();
        this.ptstn = stdNameObj.getValue();
        this.ptbcn = stdNameObj.getValue();
        this.ptscn = stdNameObj.toSmallCamelCase();
        this.ptuln = stdNameObj.toUnderLineName();
        this.ptalcn = stdNameObj.toLowerCase();
        this.ptaucn = stdNameObj.toUpperCase();
    }

}
