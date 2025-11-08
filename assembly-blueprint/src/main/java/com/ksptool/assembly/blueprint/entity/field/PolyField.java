package com.ksptool.assembly.blueprint.entity.field;

import com.ksptool.assembly.blueprint.utils.StdName;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class PolyField {

    //对应的原始字段
    @Setter
    private RawField rawField;

    //字段标准化名称(pfstn) ex: UserName
    private String stdName;

    //用于简写字段名称
    private String pfstn; //字段标准化名称(pfstn) ex: UserName
    private String pfscn; //字段小驼峰名称(pfscn) ex: userName
    private String pfbcn; //字段大驼峰名称(pfbcn) ex: UserName
    private String pfuln; //字段下划线名称(pfuln) ex: user_name
    private String pfalcn; //字段全小写名称(pfalcn) ex: username
    private String pfaucn; //字段全大写名称(pfaucn) ex: USERNAME

    //字段类型
    @Setter
    private String type;

    //字段注释
    @Setter
    private String comment;

    //必填
    @Setter
    private boolean required;

    //是否主键
    @Setter
    private boolean primaryKey;

    //排序号
    @Setter
    private int seq;

    public void setStdName(String stdName) {
        if (StringUtils.isBlank(stdName)) {
            this.stdName = stdName;
            this.pfstn = stdName;
            this.pfscn = "";
            this.pfbcn = "";
            this.pfuln = "";
            this.pfalcn = "";
            this.pfaucn = "";
            return;
        }

        StdName stdNameObj = StdName.of(stdName);
        this.stdName = stdNameObj.getValue();
        this.pfstn = stdNameObj.getValue();
        this.pfbcn = stdNameObj.getValue();
        this.pfscn = stdNameObj.toSmallCamelCase();
        this.pfuln = stdNameObj.toUnderLineName();
        this.pfalcn = stdNameObj.toLowerCase();
        this.pfaucn = stdNameObj.toUpperCase();
    }

}
