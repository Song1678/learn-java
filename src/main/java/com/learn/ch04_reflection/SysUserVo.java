package com.learn.ch04_reflection;

import java.time.LocalDateTime;

/**
 * 返回给前端的视图对象（VO，已写好）：没有 password 字段。
 * age 是 Integer（和 SysUser 的 int 类型不同），nickName 不存在，多了一个 remark。
 *
 * <p>企业项目里 Entity -> VO 的转换天天写，RuoYi-Vue-Plus 用 MapstructPlus / BeanUtil 自动完成，
 * 本质就是下面 BeanCopier 用反射做的事。
 */
public class SysUserVo {

    private Long userId;
    private String userName;
    private Integer age;
    private String remark;
    private LocalDateTime createTime;

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getAge() {
        return age;
    }

    public String getRemark() {
        return remark;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }
}
