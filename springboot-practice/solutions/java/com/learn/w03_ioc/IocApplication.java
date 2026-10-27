package com.learn.w03_ioc;

import com.learn.w03_ioc.pay.PayService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;

/**
 * w03 的独立启动类（已写好）。
 *
 * <p>本章故意放在 com.learn.web 包【外面】：主程序 WebPracticeApplication 只扫描 com.learn.web，
 * 所以你在这里练习时就算把容器搞得启动不了，也不会影响其他章节。w03 的测试用的也是这个启动类。
 * 本章用不到数据库，所以排除了数据源的自动配置（exclude = DataSourceAutoConfiguration.class）。
 *
 * <p>直接运行 main 方法：不启动 Web 服务器，只创建容器，从容器里取出 PayService 调用一下，打印结果后退出。
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class IocApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(IocApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        try (ConfigurableApplicationContext context = app.run(args)) {
            System.out.println("========== w03 IoC demo ==========");
            System.out.println("容器里一共有 " + context.getBeanDefinitionCount() + " 个 bean");
            PayService payService = context.getBean(PayService.class);
            BigDecimal amount = new BigDecimal("100.00");
            for (String type : payService.supportedTypes()) {
                System.out.println(type + " 支付 100.00 → " + payService.pay(type, amount));
            }
            System.out.println("最省钱的方式：" + payService.bestType(amount));
        }
    }
}
