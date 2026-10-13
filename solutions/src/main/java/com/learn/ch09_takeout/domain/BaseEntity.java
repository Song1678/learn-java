package com.learn.ch09_takeout.domain;

import java.time.LocalDateTime;

/**
 * 实体基类（已写好）：主键 + 创建/更新时间。
 *
 * <p>真实项目里会用 Lombok 的 @Data 自动生成 getter/setter，这里为了看清楚都手写了。
 */
public abstract class BaseEntity {

    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
