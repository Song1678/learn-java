package com.learn.ch04_reflection;

import java.time.LocalDateTime;

/**
 * 实体基类（已写好）。RuoYi-Vue-Plus 里所有数据库实体都继承一个 BaseEntity，里面放创建人、创建时间等公共字段。
 */
public class BaseEntity {

    private String createBy;
    private LocalDateTime createTime;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
