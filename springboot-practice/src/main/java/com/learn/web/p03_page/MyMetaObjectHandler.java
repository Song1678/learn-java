package com.learn.web.p03_page;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * p03 练习 3.2：自动填充 createTime / updateTime
 *
 * <p>每张表都有 create_time、update_time，每次 insert / update 都手动 set 很烦，还容易忘。
 * 实体字段加上 {@code @TableField(fill = FieldFill.INSERT)} 等注解后，MyBatis-Plus 在 insert / update 时
 * 会调用这里的方法，由我们统一填值。RuoYi-Vue-Plus 的 InjectionMetaObjectHandler 还会自动填 createBy（当前登录用户）。
 *
 * <p>注意：这个处理器是全局的，x01 综合项目也依赖它。
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 3.2 insert 时：createTime 和 updateTime 都填当前时间
     * <p>提示：this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
     * （strict 的意思是：字段有值就不覆盖，只填 null 的；字段不存在或没加 fill 注解就跳过）
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // TODO 在这里写你的代码
    }

    /**
     * 3.2 update 时：updateTime 填当前时间
     * <p>提示：this.strictUpdateFill(...)。注意 strict 系列“有值就不覆盖”，
     * 所以 Service 里不要自己 set updateTime（查出来的旧对象直接 updateById 的话，updateTime 是旧值，不会被刷新）。
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // TODO 在这里写你的代码
    }
}
