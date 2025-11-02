package com.ksptool.assembly.entity.web;

import java.util.List;

public class CommonIdDto {

    private Long id;
    private List<Long> ids;

    /**
     * 查询当前是否为批量删除模式
     */
    public boolean isBatch() {
        return ids != null && !ids.isEmpty();
    }

    public boolean isValid() {
        return id != null || (ids != null && !ids.isEmpty());
    }

    /**
     * 获取ID列表，支持批量和普通模式
     */
    public List<Long> toIds() {
        if (isBatch()) {
            return ids;
        }
        if (id != null) {
            return List.of(id);
        }
        return List.of();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
