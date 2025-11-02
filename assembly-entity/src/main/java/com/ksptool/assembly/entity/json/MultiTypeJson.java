package com.ksptool.assembly.entity.json;

import com.ksptool.entities.Entities;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class MultiTypeJson {

    //Kind 用于识别的类型
    @NotBlank(message = "Kind不可为空")
    private String k;

    //Label 用于显示的文字(可选 仅供参考 前端可能会根据kind写死文本内容)
    private String l;

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }


    /**
     * 解析JSON字符串为MultiTypeJson列表
     * 安全函数，如果解析失败，将会安全的返回空集合
     * 如果解析成功，将会返回MultiTypeJson集合
     *
     * @param jsonString JSON字符串
     * @return MultiTypeJson集合
     *
     */
    public static List<MultiTypeJson> fromJson(String jsonString) {
        return Entities.fromJsonArray(jsonString, MultiTypeJson.class);
    }

    /**
     * 将MultiTypeJson集合转换为JSON字符串
     *
     * @return JSON字符串
     */
    public static String toJson(List<MultiTypeJson> list) {
        return Entities.toJson(list);
    }
}
