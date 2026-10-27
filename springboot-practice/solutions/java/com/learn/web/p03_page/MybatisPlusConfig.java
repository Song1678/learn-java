package com.learn.web.p03_page;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * p03 练习 3.1：注册分页插件
 *
 * <p>MyBatis-Plus 的分页是靠“插件”（拦截器）实现的：执行 selectPage 时，插件会先自动执行一条
 * SELECT COUNT(*) 查总数，再在原 SQL 后面加上 LIMIT。不注册插件的话 selectPage 不会报错，
 * 但是会把所有数据都查出来，total 也是 0 —— 这是新手最常踩的坑。
 *
 * <p>注意：这个配置是全局的，x01 综合项目的分页也依赖它。
 * RuoYi-Vue-Plus 里对应的是 ruoyi-common-mybatis 模块的 MybatisPlusConfig。
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 3.1 写一个 @Bean 方法，返回 MybatisPlusInterceptor，里面加一个 PaginationInnerInterceptor(DbType.MYSQL)
     */
    // @swap
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
    //> // TODO 3.1 写一个 @Bean 方法，注册分页插件
    // @end
}
