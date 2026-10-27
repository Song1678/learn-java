package com.learn.web;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

/**
 * 启动类（已写好）。运行 main 方法就启动了一个 Web 服务器，默认端口 8080。
 *
 * <p>{@code @SpringBootApplication} = 配置类 + 自动配置 + 组件扫描（扫描本类所在包 com.learn.web 及其子包），
 * 扫到带 @Component / @Service / @RestController ... 的类就创建对象放进容器 —— 就是你在 ch08 写的 MiniContainer 做的事。
 *
 * <p>两个特殊设置（真实项目里一般不需要）：
 * <ul>
 *   <li>{@code nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class}：
 *       默认 bean 名是“类名首字母小写”，本项目不同章节有同名的类（比如好几个 DishMapper），会冲突，
 *       所以改成用“全类名”当 bean 名。</li>
 *   <li>{@code @MapperScan(annotationClass = Mapper.class)}：只把带 @Mapper 注解的接口注册成 Mapper。</li>
 * </ul>
 */
@SpringBootApplication(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@MapperScan(basePackages = "com.learn.web", annotationClass = Mapper.class,
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
public class WebPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebPracticeApplication.class, args);
    }
}
